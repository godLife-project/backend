package com.godLife.project.dto.jwtDTO;

import com.godLife.project.dto.datas.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

  private final UserDTO userDTO;

  public CustomUserDetails(UserDTO userDTO) {
    this.userDTO = userDTO;
  }

  public int getAuthorityIdx() {
    return userDTO.getAuthorityIdx();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    Collection<GrantedAuthority> collection = new ArrayList<>();

    collection.add((GrantedAuthority) () -> String.valueOf(userDTO.getAuthorityIdx()));

    return collection;
  }

  @Override
  public String getPassword() {

    return userDTO.getUserPw();
  }

  @Override
  public String getUsername() {

    return userDTO.getUserId();
  }

  @Override
  public boolean isAccountNonExpired() {

    return true;
  }

  @Override
  public boolean isAccountNonLocked() {

    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {

    return true;
  }

  @Override
  public boolean isEnabled() {

    return true;
  }

  public UserDTO getUserDTO() {
    return userDTO;
  }
}
