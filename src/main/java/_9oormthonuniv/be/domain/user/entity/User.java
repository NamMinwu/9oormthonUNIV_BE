package _9oormthonuniv.be.domain.user.entity;

import _9oormthonuniv.be.domain.post.entity.Post;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor()
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;
    private String username;
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    public void addPost(Post post) {
        posts.add(post);
        post.setUser(this);
    }


}
