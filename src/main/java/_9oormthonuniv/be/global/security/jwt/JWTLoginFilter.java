package _9oormthonuniv.be.global.security.jwt;

import _9oormthonuniv.be.global.security.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Iterator;

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final JWTTokenProvider jwtTokenProvider;

  public JWTLoginFilter(AuthenticationManager authenticationManager,
      JWTTokenProvider jwtTokenProvider) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    // 클라이언트 요청에서 받기
    String username = obtainUsername(request);
    String password = obtainPassword(request);
    // AuthenticationManager한테 넘겨주는 dto
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
        username, password, null);

    return authenticationManager.authenticate(authToken);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authentication) {
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//    String username = userDetails.();
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
    GrantedAuthority auth = iterator.next();
    String role = auth.getAuthority();
//    String token = jwtTokenProvider.generateTokenByType(username, role, 60 * 60 * 10L);
//    response.addHeader("Authorization", "Bearer " + token);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed) {
    response.setStatus(401);
  }


}
