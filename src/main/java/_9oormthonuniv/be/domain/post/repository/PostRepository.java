package _9oormthonuniv.be.domain.post.repository;

import _9oormthonuniv.be.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
