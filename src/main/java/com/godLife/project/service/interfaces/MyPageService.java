package com.godLife.project.service.interfaces;


public interface MyPageService {
    // 회원 탈퇴
    int deleteAccount(int userIdx, String userPw);
    // 탈퇴 취소
    int deleteCancelAccount(int userIdx, String userPw);
}
