package _9oormthonuniv.be.domain.auth.dto.response;

import _9oormthonuniv.be.domain.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthLoginResponseDTO {

  private String userId;
  private String role;
  private String accessToken;
  private String refreshToken;


}
