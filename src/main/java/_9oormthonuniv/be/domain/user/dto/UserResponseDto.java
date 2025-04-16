package _9oormthonuniv.be.domain.user.dto;

import _9oormthonuniv.be.domain.post.dto.respose.PostResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserResponseDto {

  private Long id;
  private String username;
  private List<PostResponseDto> posts;
}
