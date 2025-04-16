package _9oormthonuniv.be.domain.user.controller;
import _9oormthonuniv.be.domain.user.dto.CreateUserDto;
import _9oormthonuniv.be.domain.user.dto.UserResponseDto;
import _9oormthonuniv.be.domain.user.entity.User;
import _9oormthonuniv.be.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;



    @Operation(summary = "회원가입", description = "신규 유저를 등록합니다.")
    @PostMapping("/signup")
    public String createUser(CreateUserDto createUserDto) {
        userService.createUser(createUserDto);
        return "success";
    }

    @GetMapping("/{userName}")
    public ResponseEntity<UserResponseDto> getUserByUserName(@PathVariable String userName) {
        UserResponseDto user = userService.findUserByㅈUsername(userName);
        return ResponseEntity.ok(user);
    }
}
