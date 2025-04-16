package _9oormthonuniv.be.domain.post.dto.respose;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCreateResponseDto {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private String username;
}