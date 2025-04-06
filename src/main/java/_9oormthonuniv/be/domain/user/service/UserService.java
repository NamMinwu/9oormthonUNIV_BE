package _9oormthonuniv.be.domain.user.service;

import _9oormthonuniv.be.domain.user.dto.CreateUserDto;
import _9oormthonuniv.be.domain.user.entity.User;
import _9oormthonuniv.be.domain.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void createUser(CreateUserDto createUserDto) {
        String username = createUserDto.getUsername();
        String password = createUserDto.getPassword();

        Boolean isExists = userRepository.existsByUsername(username);

        if (isExists) {
            return;
        }

        User user = User.builder().username(username).password(bCryptPasswordEncoder.encode(password)).role("ROLE_ADMIN").build();
        userRepository.save(user);
    }
}
