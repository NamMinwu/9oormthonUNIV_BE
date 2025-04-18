package _9oormthonuniv.be.domain.auth.controller;

import _9oormthonuniv.be.domain.auth.dto.request.AuthLoginRequestDTO;
import _9oormthonuniv.be.domain.auth.dto.request.TokenRefreshRequestDTO;
import _9oormthonuniv.be.domain.auth.dto.response.AuthLoginResponseDTO;
import _9oormthonuniv.be.domain.auth.dto.response.TokenRefreshResponseDTO;
import _9oormthonuniv.be.domain.auth.service.AuthService;
import _9oormthonuniv.be.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<AuthLoginResponseDTO> login(
      @RequestBody AuthLoginRequestDTO loginRequestDto
  ) {
    return ResponseEntity.ok(
        authService.login(loginRequestDto)
    );
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    // 현재 인증된 사용자 정보에서 userId 추출
    String userId = userDetails.getUsername();

    // 로그아웃 처리
    authService.logout(userId);

    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @PostMapping("/token/refresh")
  public ResponseEntity<TokenRefreshResponseDTO> refreshToken(
      @Valid @RequestBody TokenRefreshRequestDTO refreshRequestDto
  ) {
    return ResponseEntity.ok(
        authService.getNewRefreshToken(refreshRequestDto)
    );
  }


}
