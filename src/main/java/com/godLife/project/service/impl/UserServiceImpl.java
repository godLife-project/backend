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
            // 닉네임 중복 시
            int duplicate = userMapper.checkUserNickExist(joinUserDTO.getUserNick());
            String tag = "#" + (duplicate + 1);
            joinUserDTO.setNickTag(tag);

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

    // 유저 정보 조회
    @Override
    public UserDTO findByUserId(String userId) { return userMapper.findByUserid(userId); }

}
