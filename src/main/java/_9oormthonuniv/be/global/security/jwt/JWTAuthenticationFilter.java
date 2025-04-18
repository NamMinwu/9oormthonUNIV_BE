package _9oormthonuniv.be.global.security.jwt;

import _9oormthonuniv.be.domain.user.entity.Role;
import _9oormthonuniv.be.global.security.CustomUserDetails;
import _9oormthonuniv.be.domain.user.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

  private final JWTTokenProvider jwtTokenProvider;

  public JWTAuthenticationFilter(JWTTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String token = jwtTokenProvider.resolveToken((HttpServletRequest) request); // 헤더에서 토큰을 받아옴
    if (token != null && jwtTokenProvider.validateToken(token)) {
      Authentication authToken = getAuthentication(token);
      // 세션에 사용자 등록
      SecurityContextHolder.getContext().setAuthentication(authToken);
    }
    filterChain.doFilter(request, response);

  }

  private Authentication getAuthentication(String token) {
    String userId = jwtTokenProvider.getUserId(token);
    String role = jwtTokenProvider.getRole(token);
    // 클레임에서 권한 정보 가져오기
    Collection<? extends GrantedAuthority> authorities =
        Collections.singletonList(new SimpleGrantedAuthority(role));
    String roleString = authorities.stream()
        .findFirst()
        .map(GrantedAuthority::getAuthority)
        .orElseThrow(() -> new RuntimeException("No authority found"));

    Role changeRole = Role.valueOf(roleString);

    User user = User.builder().id(Long.parseLong(userId)).role(
        changeRole
    ).build();
    CustomUserDetails customUserDetails = new CustomUserDetails(user);

    // 스프링 시큐리티 인증 토큰 생성
    // 세션에 사용자 등록
    return new UsernamePasswordAuthenticationToken(customUserDetails, null,
        customUserDetails.getAuthorities());
  }
}
