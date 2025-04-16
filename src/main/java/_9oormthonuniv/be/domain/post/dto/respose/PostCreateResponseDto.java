package _9oormthonuniv.be.domain.post.dto.respose;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class PostCreateResponseDto {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private String username;

    @Builder
    public PostCreateResponseDto(Long id, String title, String content, String imageUrl, String username) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.username = username;
    }


}