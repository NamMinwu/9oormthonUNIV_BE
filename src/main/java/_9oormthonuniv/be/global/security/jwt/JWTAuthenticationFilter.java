package _9oormthonuniv.be.global.security.jwt;

import _9oormthonuniv.be.domain.user.entity.Role;
import _9oormthonuniv.be.domain.user.repository.UserRepository;
import _9oormthonuniv.be.domain.user.service.UserService;
import _9oormthonuniv.be.global.security.CustomUserDetails;
import _9oormthonuniv.be.domain.user.entity.User;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@RequiredArgsConstructor
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {

  private final UserRepository userRepository;
  private final JWTTokenProvider jwtTokenProvider;


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String token = jwtTokenProvider.resolveToken(request); // 헤더에서 토큰을 추출

    try {

      if (token != null && jwtTokenProvider.validateToken(token)) {
        Authentication authToken = getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authToken); // 인증 객체 설정
      }
    } catch (JwtException | IllegalArgumentException e) {
      // JWT 관련 에러 처리
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json;charset=UTF-8");
      response.getWriter().write("{\"error\": \"Invalid or expired JWT token\"}");
      return;
    }

    filterChain.doFilter(request, response); // 다음 필터로 전달
  }

  private Authentication getAuthentication(String token) {
    String userId = jwtTokenProvider.getUserId(token);

    User user = userRepository.findById(Long.parseLong(userId)); // Member 객체 조회

    CustomUserDetails customUserDetails = new CustomUserDetails(user);

    // 스프링 시큐리티 인증 토큰 생성
    // 세션에 사용자 등록
    return new UsernamePasswordAuthenticationToken(customUserDetails, null,
        customUserDetails.getAuthorities());
  }
}
