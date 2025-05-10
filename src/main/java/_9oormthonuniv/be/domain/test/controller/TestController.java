package _9oormthonuniv.be.domain.test.controller;

import java.io.IOException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

  // 예시로 나누기 연산을 수행하는 메서드
  @GetMapping("/divide")
  public String divide(@RequestParam int dividend, @RequestParam int divisor) throws IOException {
    if (divisor == 0) {
      throw new IOException("이미지가 업로드가 안되었습니다.");
    }
    return "Result: " + (dividend / divisor);
  }


}