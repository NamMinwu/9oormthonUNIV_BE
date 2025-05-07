package _9oormthonuniv.be.domain.post.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PostKafkaMessageRequestDto {

  @JsonProperty("postId")
  private Long postId;

  @JsonProperty("title")
  private String title;

  @JsonProperty("username")
  private String username;
}
