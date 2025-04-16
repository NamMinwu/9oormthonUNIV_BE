package _9oormthonuniv.be.domain.post.controller;

import _9oormthonuniv.be.domain.post.dto.request.PostCreateRequestDto;
import _9oormthonuniv.be.domain.post.dto.respose.PostCreateResponseDto;
import _9oormthonuniv.be.domain.post.dto.respose.PostResponseDto;
import _9oormthonuniv.be.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    // 게시글 생성
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostCreateResponseDto> createPost(@ModelAttribute PostCreateRequestDto request) throws IOException {
        PostCreateResponseDto responseDto = postService.create(request);
        return ResponseEntity.ok(responseDto);
    }

    


}
