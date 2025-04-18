package _9oormthonuniv.be.global.security;

import _9oormthonuniv.be.domain.user.entity.User;
import _9oormthonuniv.be.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // username이 의미하는 것은 Id

    User user = this.userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("해당 ID의 유저가 없습니다"));

    return new CustomUserDetails(user);

  }
}
