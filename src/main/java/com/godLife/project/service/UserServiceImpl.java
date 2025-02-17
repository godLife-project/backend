package com.godLife.project.service;



import com.godLife.project.dto.datas.UserDTO;
import com.godLife.project.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    // 회원가입
    @Override
    public String insertUser(UserDTO joinUserDTO) {
        try {
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
