package _9oormthonuniv.be.domain.post.dto.respose;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class PostResponseDto {

  private Long id;
  private String title;
  private String content;
  private String imageUrl;

  @Builder
  public PostResponseDto(Long id, String title, String content, String imageUrl) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.imageUrl = imageUrl;
  }
}
