package _9oormthonuniv.be.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoginType {
  KAKAO("카카오"),
  GOOGLE("구글"),
  NAVER("네이버"),
  APPLE("애플"),
  ANOYMOUS("익명");
  private final String toKorean;
}