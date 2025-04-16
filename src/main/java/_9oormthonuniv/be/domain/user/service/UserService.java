package _9oormthonuniv.be.domain.user.service;

import _9oormthonuniv.be.domain.post.dto.respose.PostResponseDto;
import _9oormthonuniv.be.domain.user.dto.CreateUserDto;
import _9oormthonuniv.be.domain.user.dto.UserResponseDto;
import _9oormthonuniv.be.domain.user.entity.User;
import _9oormthonuniv.be.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional()
    public UserResponseDto findUserByUsername(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return null;
        }

        List<PostResponseDto> posts = user.getPosts().stream().map(post ->
                new PostResponseDto(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getImageUrl()
                )
        ).collect(Collectors.toList());

        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                posts
        );
    }
}
