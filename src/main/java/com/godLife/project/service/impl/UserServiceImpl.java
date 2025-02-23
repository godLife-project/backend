package com.godLife.project.service.impl;


import com.godLife.project.dto.datas.UserDTO;
import com.godLife.project.mapper.UserMapper;
import com.godLife.project.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder)
    {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입
    @Override
    public String insertUser(UserDTO joinUserDTO) {
        try {
            String encryptedPassword = passwordEncoder.encode(joinUserDTO.getUserPw());
            joinUserDTO.setUserPw(encryptedPassword);
            //System.out.println(joinUserDTO);
            userMapper.insertUser(joinUserDTO);
            return "Success";
        } catch (Exception e) {
            return "Error : " + e.getMessage();
        }
    }

    // 아이디 중복 체크
    @Override
    public Boolean checkUserIdExist(String userId) {
        return userMapper.checkUserIdExist(userId);
    }

}
