package com.godLife.project.service;


import com.godLife.project.dto.datas.UserDTO;
import com.godLife.project.exception.LoginFailedException;
import com.godLife.project.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder)
    {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // 사용자 정보만 조회
    @Override
    public UserDTO getUserLogin(String userId) {
        return userMapper.UserLogin(userId);  // userId로만 사용자 정보 조회
    }

    // 로그인 처리 (비밀번호까지 검증)
    @Override
    public UserDTO login(String userId, String userPw) {
        // 사용자 정보 조회
        UserDTO user = userMapper.UserLogin(userId);
        System.out.println("DB에서 찾은 사용자: " + user);
        // 사용자가 존재하지 않거나 비밀번호가 일치하지 않으면 로그인 실패
        if (user == null || !passwordEncoder.matches(userPw, user.getUserPw())) {  // 평문 비밀번호 비교
            // 커스텀 예외를 사용하거나 적절한 예외를 던질 수 있음
            throw new LoginFailedException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        return user;  // 로그인 성공 시 사용자 정보 반환
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
