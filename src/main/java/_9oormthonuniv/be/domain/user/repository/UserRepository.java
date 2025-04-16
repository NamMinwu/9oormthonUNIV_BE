package _9oormthonuniv.be.domain.user.repository;

import _9oormthonuniv.be.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  Boolean existsByUsername(String username);

  User findByUsername(String username);

  User findById(long id);
}
