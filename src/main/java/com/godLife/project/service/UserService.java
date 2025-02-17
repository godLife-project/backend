package com.godLife.project.service;

import com.godLife.project.dto.datas.UserDTO;

public interface UserService {
    // 회원가입
    String insertUser(UserDTO joinUserDTO);
    // 아이디 중복 체크
    Boolean checkUserIdExist(String userId);
}
