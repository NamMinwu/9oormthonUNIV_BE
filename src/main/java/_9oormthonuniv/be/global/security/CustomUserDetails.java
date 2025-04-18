package _9oormthonuniv.be.global.security;

import _9oormthonuniv.be.domain.user.entity.Role;
import _9oormthonuniv.be.domain.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

  private final User user;


  public CustomUserDetails(User user) {
    this.user = user;

  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> collection = new ArrayList<>();
    // USER, ADMIn 따라 권한 설정하기 위해, authorities에 추가
    collection.add(new SimpleGrantedAuthority(user.getRole().toString()));
    return collection;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getId().toString();
  }
}
