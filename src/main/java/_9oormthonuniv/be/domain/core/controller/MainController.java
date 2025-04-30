package _9oormthonuniv.be.domain.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/test")
public class MainController {

  @GetMapping("/")
  public String mainPage() {
    return "He ll o W o r ld !  W e lcom  e t e s cd d cㄴd ";
  }

  @GetMapping("/new")
  public String testPage() {
    return "He ll o W o r ld !  W e lcom  e t e s cd d cㄴd ";
  }
}
