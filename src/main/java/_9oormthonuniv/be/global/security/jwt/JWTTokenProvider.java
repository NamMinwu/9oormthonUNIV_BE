package _9oormthonuniv.be.global.security.jwt;

import _9oormthonuniv.be.global.security.jwt.dto.TokenDTO;
import _9oormthonuniv.be.global.security.jwt.dto.TokenType;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import org.springframework.util.StringUtils;

@Component
public class JWTTokenProvider {

  private SecretKey secretKey;

  @Value("${jwt.token.access_expiration}")
  private Long accessExpiration;

  @Value("${jwt.token.refresh_expiration}")
  private Long refreshExpiration;

  public JWTTokenProvider(@Value("${jwt.secret}") String secret) {

    secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
        Jwts.SIG.HS256.key().build().getAlgorithm());
  }


  public String getUserId(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
        .get("userId", String.class);
  }

  public String getRole(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
        .get("role", String.class);
  }


  private Date calculateExpirationDate(Date createdDate, long jwtExpiration) {
    return new Date(createdDate.getTime() + jwtExpiration);
  }

  public String generateTokenByType(String userId,
      String role, TokenType tokenType) {
    Date now = new Date();
    Date expiration;
    // 분기 나눠야 함, 리프레쉬 토큰과 액세스 토큰의 만료시간이 다르니까
    if (TokenType.ACCESS.equals(tokenType)) { // 액세스 토큰
      expiration = calculateExpirationDate(now, accessExpiration);
    } else { // 리프레쉬 토큰
      expiration = calculateExpirationDate(now, refreshExpiration);
    }

    return Jwts.builder()
        .claim("userId", userId)
        .claim("role", role)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(expiration)
        .signWith(secretKey)
        .compact();
  }

  // 유저 정보를 가지고 AccessToken, RefreshToken을 생성
  public TokenDTO generateToken(String userId, String role) {
    String accessToken = generateTokenByType(userId, role, TokenType.ACCESS);
    String refreshToken = generateTokenByType(userId, role, TokenType.REFRESH);

    return TokenDTO.builder().accessToken(accessToken).refreshToken(refreshToken)
        .grantType("Bearer").build();
  }

  // 토큰 유효성 검사
  public boolean validateToken(String token) {
    try {
      // 여기서 parseSignedClaims()를 사용해야 서명된 JWT 파싱
      Jwts.parser()
          .verifyWith(secretKey) // 서명 키 지정
          .build()
          .parseSignedClaims(token); // 토큰 유효성 및 서명 확인
      return true;
    } catch (JwtException | SecurityException | IllegalArgumentException e) {
      return false;
    }
  }

  // 토큰에서 회원 아이디 추출
  public String resolveToken(HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
      return token.split(" ")[1];
    }
    return null;
  }


}