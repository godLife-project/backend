package com.godLife.project.service.interfaces;

import com.godLife.project.dto.datas.UserDTO;
import com.godLife.project.dto.request.GetNameNEmail;

public interface UserService {
    // 회원가입
    String insertUser(UserDTO joinUserDTO);
    // 아이디 중복 체크
    Boolean checkUserIdExist(String userId);
    // 유저 정보 조회
    UserDTO findByUserId(String userId);

    // 아이디 찾기
    String FindUserIdByNameNEmail(GetNameNEmail getNameNEmail, boolean isMasked);

}
