package _9oormthonuniv.be.domain.refresh_token.service;

import _9oormthonuniv.be.domain.refresh_token.entity.RefreshToken;
import _9oormthonuniv.be.domain.refresh_token.repository.RefreshTokenRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final RefreshTokenRepository repository;

  public void save(Long userId, String token, long validityMillis) {
    repository.save(RefreshToken.builder()
        .userId(userId)
        .token(token)
        .expiresAt(LocalDateTime.now().plusSeconds(validityMillis / 1000))
        .build());
  }

  public boolean isValid(Long userId) {
    return repository.existsByUserId(userId);
  }

  public void delete(Long userId) {
    repository.deleteByUserId(userId);
  }

  public void update(Long userId, String newToken, long validityMillis) {
    repository.save(RefreshToken.builder()
        .userId(userId)
        .token(newToken)
        .expiresAt(LocalDateTime.now().plusSeconds(validityMillis / 1000))
        .build());
  }

}
