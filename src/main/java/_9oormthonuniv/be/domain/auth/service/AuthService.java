package _9oormthonuniv.be.domain.auth.service;

import _9oormthonuniv.be.domain.auth.dto.request.AuthLoginRequestDTO;
import _9oormthonuniv.be.domain.auth.dto.request.TokenRefreshRequestDTO;
import _9oormthonuniv.be.domain.auth.dto.response.AuthLoginResponseDTO;
import _9oormthonuniv.be.domain.auth.dto.response.TokenRefreshResponseDTO;
import _9oormthonuniv.be.domain.refresh_token.entity.RefreshToken;
import _9oormthonuniv.be.domain.refresh_token.repository.RefreshTokenRepository;
import _9oormthonuniv.be.domain.refresh_token.service.RefreshTokenService;
import _9oormthonuniv.be.domain.user.entity.Role;
import _9oormthonuniv.be.domain.user.repository.UserRepository;
import _9oormthonuniv.be.global.security.CustomUserDetails;
import _9oormthonuniv.be.global.security.jwt.JWTTokenProvider;
import _9oormthonuniv.be.global.security.jwt.dto.TokenDTO;
import jakarta.security.auth.message.AuthException;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

  private final RefreshTokenService refreshTokenService;
  private final RefreshTokenRepository refreshTokenRepository;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final JWTTokenProvider jwtTokenProvider;


  public AuthLoginResponseDTO login(AuthLoginRequestDTO authLoginRequestDTO) {
    // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
    // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        authLoginRequestDTO.getUsername(),
        authLoginRequestDTO.getPassword());

    // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
    // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
    Authentication authentication = authenticationManagerBuilder.getObject()
        .authenticate(authenticationToken);

    CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
    String userId = customUserDetails.getUsername();
    Collection<? extends GrantedAuthority> userRole = customUserDetails.getAuthorities();

    // 3. 인증 정보를 기반으로 JWT 토큰 생성
    TokenDTO tokenDto = jwtTokenProvider.generateToken(userId,
        userRole.iterator().next().getAuthority());
    // 리프래시 토큰 저장
    refreshTokenService.save(Long.parseLong(userId),
        tokenDto.getRefreshToken(), 259200000);

    return AuthLoginResponseDTO.builder().role(
        userRole.iterator().next().getAuthority()
    ).refreshToken(
        tokenDto.getRefreshToken()
    ).accessToken(
        tokenDto.getAccessToken()
    ).userId(
        userId
    ).build();
  }

  public void logout(String userId) {
    refreshTokenService.delete(Long.parseLong(userId));

  }

  @Transactional
  public TokenRefreshResponseDTO getNewRefreshToken(
      TokenRefreshRequestDTO tokenRefreshRequestDTO
  ) {
    String refreshToken = tokenRefreshRequestDTO.getRefreshToken();

    // refresh 만기 시간 확인
    if (jwtTokenProvider.validateToken(refreshToken)) {
      String userId = jwtTokenProvider.getUserId(refreshToken);

      String role = jwtTokenProvider.getRole(refreshToken);

      RefreshToken savedRefreshToken = refreshTokenRepository.findByUserId(
              Long.parseLong(userId))
          .orElseThrow(() -> new BadCredentialsException(
              "로그인 한 이력이 없습니다. 다시 로그인 해주세요"
          ));
      if (!savedRefreshToken.getToken().equals(refreshToken)) {
        throw new BadCredentialsException("유효하지 않는 리프래시 토큰 입니다.");
      }

      // 이 아래에서 savedRefreshToken을 사용해서 accessToken 재발급 로직 등 추가
      TokenDTO newTokens = jwtTokenProvider.generateToken(userId, role);

      refreshTokenService.update(Long.parseLong(userId), newTokens.getRefreshToken(), 259200000);
      return TokenRefreshResponseDTO
          .builder()
          .accessToken(newTokens.getAccessToken())
          .build();
    } else {
      throw new BadCredentialsException("유효하지 않은 토큰입니다. 다시 로그인 해주세요.");
    }
  }

}
