package _9oormthonuniv.be.global.security.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenType {
  ACCESS("액세스 토큰"), REFRESH("리프레쉬 토큰"),
  ;
  final String value;
}