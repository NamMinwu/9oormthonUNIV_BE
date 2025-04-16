package _9oormthonuniv.be.domain.post.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@NoArgsConstructor
public class PostCreateRequestDto {
    private String title;
    private String content;
    private Long userId; // 연결할 유저 ID
    private MultipartFile imageFile;
}

