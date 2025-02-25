package com.godLife.project.service.impl.jwtImpl;

import com.godLife.project.dto.jwtDTO.CustomUserDetails;
import com.godLife.project.dto.datas.UserDTO;
import com.godLife.project.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService  implements UserDetailsService {

  private final UserMapper userMapper;

  public CustomUserDetailsService(UserMapper userMapper) {

    this.userMapper = userMapper;
  }

  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

    //DB에서 조회
    UserDTO userData = userMapper.findByUserid(userId);
    //System.out.println("========================================  유저 조회  =======================================");
    //System.out.println(userData);
    //System.out.println("============================================================================================");
    if (userData != null) {

      //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
      return new CustomUserDetails(userData);
    }

    return null;
  }
}
