package com.godLife.project.service;

import com.godLife.project.dto.datas.UserDTO;

public interface UserService {
    // 사용자 정보만 조회 (userId로 조회)
    UserDTO getUserLogin(String userId);

    // 로그인 처리 (userId와 userPw로 로그인 검증
    UserDTO login(String userId, String userPw);

}
