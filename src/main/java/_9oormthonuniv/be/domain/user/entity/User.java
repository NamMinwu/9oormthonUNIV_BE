package _9oormthonuniv.be.domain.user.entity;

import _9oormthonuniv.be.global.common.entity.BaseEntity;
import _9oormthonuniv.be.domain.post.entity.Post;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor()
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(nullable = false)
  private String password;

  @NotNull
  @Column(nullable = false)
  private String username;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @Enumerated(EnumType.STRING)
  private LoginType loginType;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Post> posts = new ArrayList<>();

  public void addPost(Post post) {
    posts.add(post);
    post.setUser(this);
  }


}
