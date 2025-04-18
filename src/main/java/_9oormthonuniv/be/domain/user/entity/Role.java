package _9oormthonuniv.be.domain.user.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
  ADMIN("관리자"),
  USER("유저");
  private final String toKorean;
}