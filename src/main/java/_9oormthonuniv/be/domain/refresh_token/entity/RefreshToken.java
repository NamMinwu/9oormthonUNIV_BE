package _9oormthonuniv.be.domain.refresh_token.entity;

import _9oormthonuniv.be.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken extends BaseEntity {

  @Id
  private Long userId;
  @Column(nullable = false)
  private String token;
  @Column(nullable = false)
  private LocalDateTime expiresAt;


}
