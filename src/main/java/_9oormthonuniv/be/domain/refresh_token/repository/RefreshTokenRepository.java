package _9oormthonuniv.be.domain.refresh_token.repository;

import _9oormthonuniv.be.domain.refresh_token.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  boolean existsByUserId(Long userId);

  Optional<RefreshToken> findByUserId(Long userId);

  void deleteByUserId(Long userId);


}
