package com.godLife.project.service.impl;

import com.godLife.project.mapper.MyPageMapper;
import com.godLife.project.service.interfaces.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageServiceImpl implements MyPageService {

    private final MyPageMapper myPageMapper;

    private final AuthServiceImpl authService;

    // 회원 탈퇴
    @Override
    public int deleteAccount(int userIdx, String userPw) {
        try {
            String encryptPassword = myPageMapper.getEncryptPassword(userIdx);

            if (encryptPassword == null) { return 404; } // 비밀번호 조회 안될 경우

            boolean isValid = authService.verifyPassword(userPw, encryptPassword);

            if (isValid) {
                int result = myPageMapper.deleteAccount(userIdx);

                if (result <= 0) { return 404; } // 이미 탈퇴 했거나, 유저 정보 없는 경우

                return 200; // 탈퇴 완료
            }

            return 403; // 비밀번호 틀림

        } catch (Exception e) {
            log.error("e: ", e);
            return 500;
        }
    }

    // 탈퇴 취소
    @Override
    public int deleteCancelAccount(int userIdx, String userPw) {
        try {
            String encryptPassword = myPageMapper.getEncryptPassword(userIdx);

            if (encryptPassword == null) { return 404; } // 비밀번호 조회 안될 경우

            boolean isValid = authService.verifyPassword(userPw, encryptPassword);
            if (isValid) {
                int result = myPageMapper.deleteCancelAccount(userIdx);

                if (result <= 0) { return 404; } // 이미 완전 삭제 했거나, 유저 정보 없는 경우

                return 200; // 취소 완료
            }

            return 403; // 비밀번호 틀림

        } catch (Exception e) {
            log.error("e: ", e);
            return 500;
        }
    }



}
