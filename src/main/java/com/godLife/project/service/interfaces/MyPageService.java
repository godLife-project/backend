package com.godLife.project.service.interfaces;


import com.godLife.project.dto.datas.UserDTO;
import com.godLife.project.dto.request.myPage.*;
import com.godLife.project.dto.response.MyPageUserInfosResponseDTO;

import java.util.List;

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
    // 직업/목표 수정
    int modifyJobTarget(ModifyJobTargetRequestDTO modifyJobTargetRequestDTO);

    // 비밀번호 수정
    int modifyPassword(GetUserPwRequestDTO getUserPwRequestDTO);

    // 루틴 일괄 삭제
    int deleteSelectPlans(int userIdx, List<Integer> planIndexes);
    // 루틴 일괄 비/공개 전환
    int switchIsSharedBySelectPlans(int userIdx, List<Integer> planIndexes, String mode);

    // 루틴 일괄 좋아요 취소
    int unLikeSelectPlans(int userIdx, List<Integer> planIndexes);

}
