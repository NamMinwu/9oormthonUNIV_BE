package _9oormthonuniv.be.domain.post.service;

import _9oormthonuniv.be.domain.kafka.service.KafkaProducerService;
import _9oormthonuniv.be.domain.post.dto.request.PostCreateRequestDto;
import _9oormthonuniv.be.domain.post.dto.request.PostKafkaMessageRequestDto;
import _9oormthonuniv.be.domain.post.dto.respose.PostCreateResponseDto;
import _9oormthonuniv.be.domain.post.dto.respose.PostResponseDto;
import _9oormthonuniv.be.domain.post.entity.Post;
import _9oormthonuniv.be.domain.post.repository.PostRepository;
import _9oormthonuniv.be.domain.s3.service.S3Service;
import _9oormthonuniv.be.domain.user.entity.User;
import _9oormthonuniv.be.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final S3Service s3Service;
  private final KafkaProducerService kafkaProducerService;

  @Transactional
  public PostCreateResponseDto create(PostCreateRequestDto requestDto) throws IOException {
    User user = userRepository.findById(requestDto.getUserId()).orElseThrow(
        () -> new IllegalArgumentException("User not found")
    );

    // 이미지 업로드
    String imageUrl = s3Service.uploadFile(requestDto.getImageFile());

    if (imageUrl == null) {
      throw new IOException("이미지가 업로드가 안되었습니다.");
    }

    // Post 엔티티 생성
    Post post = Post.builder()
        .title(requestDto.getTitle())
        .content(requestDto.getContent())
        .imageUrl(imageUrl)
        .build();

    // 양방향 연관관계 설정
    user.addPost(post);

    // 저장 (cascade = ALL 이므로 user 저장 시 post도 저장됨)
    userRepository.save(user);

    // Kafka 메시지 객체 생성
    PostKafkaMessageRequestDto message = new PostKafkaMessageRequestDto(post.getId(),
        post.getTitle(),
        user.getUsername());
    // Kafka로 메시지 전송
    kafkaProducerService.sendTopic(message);

    return PostCreateResponseDto.builder()
        .id(post.getId())
        .title(post.getTitle())
        .content(post.getContent())
        .imageUrl(post.getImageUrl())
        .username(user.getUsername())
        .build();
  }

  public List<PostResponseDto> getAll() {
    List<Post> posts = postRepository.findAll();
    return posts.stream()
        .map(post -> PostResponseDto.builder()
            .id(post.getId())
            .title(post.getTitle())
            .content(post.getContent())
            .imageUrl(post.getImageUrl())
            .build())
        .collect(Collectors.toList());
  }
}
