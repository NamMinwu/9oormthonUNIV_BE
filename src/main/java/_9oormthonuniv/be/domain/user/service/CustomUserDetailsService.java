package _9oormthonuniv.be.domain.user.service;

import _9oormthonuniv.be.domain.user.dto.CustomUserDetails;
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
        User user = this.userRepository.findByUsername(username);

        if (user != null) {
            return new CustomUserDetails(user);
        }

        return null;
    }
}
