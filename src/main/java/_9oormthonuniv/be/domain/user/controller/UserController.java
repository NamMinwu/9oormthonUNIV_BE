package _9oormthonuniv.be.domain.user.controller;
import _9oormthonuniv.be.domain.user.dto.CreateUserDto;
import _9oormthonuniv.be.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "회원 관련 API")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @Operation(summary = "회원가입", description = "신규 유저를 등록합니다.")
    @PostMapping("/signup")
    public String createUser(CreateUserDto createUserDto) {
        userService.createUser(createUserDto);
        return "success";
    }
}
