package com.godLife.project.service.interfaces;


import com.godLife.project.dto.datas.UserDTO;
import com.godLife.project.dto.request.myPage.ModifyEmailRequestDTO;
import com.godLife.project.dto.request.myPage.ModifyNicknameRequestDTO;
import com.godLife.project.dto.request.myPage.ModifyPersonalRequestDTO;
import com.godLife.project.dto.response.MyPageUserInfosResponseDTO;

public interface MyPageService {
    // 회원 탈퇴
    int deleteAccount(int userIdx, String userPw);
    // 탈퇴 취소
    int deleteCancelAccount(int userIdx, String userPw);

    // 회원 정보 조회
    MyPageUserInfosResponseDTO getUserInfos(int userIdx);
    // 회원 정보 수정
    int modifyPersonal(ModifyPersonalRequestDTO modifyPersonalRequestDTO);
    // 닉네임 수정
    int modifyNickName(ModifyNicknameRequestDTO modifyNicknameRequestDTO);
    // 이메일 수정
    int modifyEmail(ModifyEmailRequestDTO modifyEmailRequestDTO);
}
