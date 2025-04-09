package com.godLife.project.service.impl;

import com.godLife.project.dto.datas.UserDTO;
import com.godLife.project.dto.request.myPage.*;
import com.godLife.project.dto.response.MyPageUserInfosResponseDTO;
import com.godLife.project.mapper.MyPageMapper;
import com.godLife.project.mapper.UserMapper;
import com.godLife.project.service.interfaces.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageServiceImpl implements MyPageService {

    private final MyPageMapper myPageMapper;
    private final UserMapper userMapper;

    private final AuthServiceImpl authService;
    private final PasswordEncoder passwordEncoder;

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

    // 유저 정보 조회
    @Override
    public MyPageUserInfosResponseDTO getUserInfos(int userIdx) {
        MyPageUserInfosResponseDTO infos = myPageMapper.getUserInfos(userIdx);

        if (infos == null) { return null; }

        // 이메일 마스킹 처리
        String email = infos.getUserEmail();
        if (email != null && email.contains("@")) { // thisistest@teeest.com
            String[] parts = email.split("@"); // thisistest, teeest.com
            String username = parts[0]; // thisistest
            String domain = parts[1]; // teeest.com

            String usernameMasked = (username.length() <= 2) ? username : username.substring(0, 2) + "*".repeat(username.length() - 2);
            int dotIndex = domain.indexOf(".");
            String domainMasked = (dotIndex > 1) ? domain : domain.charAt(0) + "*".repeat(dotIndex - 1) +domain.substring(dotIndex);
            // 마스킹 이메일 저장
            infos.setUserEmail(usernameMasked + "@" + domainMasked);
        }

        // 전화번호 마스킹 처리
        String phone = infos.getUserPhone();
        String[] parts = phone.split("-");

        if (parts.length == 3) {
            String firstPart = parts[0]; // 지역번호 또는 앞자리 (02, 010, 032 등)
            String secondPart = parts[1]; // 가운데 번호
            String thirdPart = parts[2]; // 마지막 번호

            // 가운데 자리 마스킹 (첫 글자만 남기고 * 처리)
            String maskedSecond = secondPart.charAt(0) + "*".repeat(secondPart.length() - 1);
            // 마지막 자리 마스킹 (첫 글자만 남기고 * 처리)
            String maskedThird = thirdPart.charAt(0) + "*".repeat(thirdPart.length() - 1);

            // 마스킹 전화번호 저장
            infos.setUserPhone(firstPart + "-" + maskedSecond + "-" + maskedThird);
        }
        return infos;
    }

    // 개인정보 수정
    @Override
    public int modifyPersonal(ModifyPersonalRequestDTO modifyPersonalRequestDTO) {
        try {
            int result = myPageMapper.modifyPersonal(modifyPersonalRequestDTO);

            if (result == 0) { return 404; }

            return 200;
        } catch (Exception e) {
            log.error("e: ", e);
            return 500;
        }
    }

    // 닉네임 수정
    @Override
    public int modifyNickName(ModifyNicknameRequestDTO modifyNicknameRequestDTO) {
        try {
            // 닉네임 중복 시
            int duplicate = userMapper.checkUserNickExist(modifyNicknameRequestDTO.getUserNick());
            String tag = "#" + (duplicate + 1);
            modifyNicknameRequestDTO.setNickTag(tag);

            int result = myPageMapper.modifyNickName(modifyNicknameRequestDTO);

            if (result == 0) { return 404; }

            return 200;
        } catch (Exception e) {
            log.error("e: ", e);
            return 500;
        }
    }

    // 이메일 수정
    @Override
    public int modifyEmail(ModifyEmailRequestDTO modifyEmailRequestDTO) {
        try {
            int result = myPageMapper.modifyEmail(modifyEmailRequestDTO);

            if (result == 0) { return 404; }

            return 200;
        } catch (Exception e) {
            log.error("e: ", e);
            return 500;
        }
    }

    // 직업/목표 수정
    @Override
    public int modifyJobTarget(ModifyJobTargetRequestDTO modifyJobTargetRequestDTO) {
        try {
            int result = myPageMapper.modifyJobTarget(modifyJobTargetRequestDTO);

            if (result == 0) { return 404; }

            return 200;
        } catch (Exception e) {
            log.error("e: ", e);
            return 500;
        }
    }

    // 비밀번호 수정
    @Override
    public int modifyPassword(GetUserPwRequestDTO userPwRequestDTO) {
        try {
            String originalPw = userPwRequestDTO.getOriginalPw();
            String userPw = userPwRequestDTO.getUserPw();
            String pwConfirm = userPwRequestDTO.getUserPwConfirm();
            int userIdx = userPwRequestDTO.getUserIdx();

            String encryptPassword = myPageMapper.getEncryptPassword(userIdx);
            if (encryptPassword == null) { return 404; } // 비밀번호 조회 안될 경우

            boolean isValid = authService.verifyPassword(originalPw, encryptPassword);
            // 현재 유저의 암호 틀림
            if (!isValid) { return 403; }
            // 현재 암호와 변경 암호 일치 여부
            if (originalPw.equals(userPw)) { return 409; }
            // 비밀번호 확인 존재 여부
            if (pwConfirm == null || pwConfirm.isBlank()) { return 400; }
            // 변경 비밀번호와 비밀번호 확인 일치 여부
            if (!userPw.equals(pwConfirm)) { return 422; }

            String encryptedPassword = passwordEncoder.encode(userPwRequestDTO.getUserPw());
            userPwRequestDTO.setUserPw(encryptedPassword);

            int result = myPageMapper.modifyPassword(userPwRequestDTO);

            if (result == 0) {
                return 404;
            }

            return 200;
        } catch (Exception e) {
            log.error("e: ", e);
            return 500;
        }
    }

    // 루틴 일괄 삭제
    @Override
    public int deleteSelectPlans(int userIdx, List<Integer> planIndexes) {
        try {
            int result = myPageMapper.deleteSelectPlans(userIdx, planIndexes);

            if (result == 0) {
                return 404;
            }
            return 200;
        } catch (Exception e) {
            log.error("e:" , e);
            return 500;
        }
    }

    // 선택 루틴 일괄 비/공개 전환
    @Override
    public int switchIsSharedBySelectPlans(int userIdx, List<Integer> planIndexes, String mode) {
        try {
            int result = myPageMapper.switchIsSharedBySelectPlans(userIdx, planIndexes, mode);

            if (result == 0) {
                return 404;
            }
            return 200;
        } catch (Exception e) {
            log.error("e:" , e);
            return 500;
        }
    }

    // 선택 루틴 일괄 좋아요 취소
    @Override
    public int unLikeSelectPlans(int userIdx, List<Integer> planIndexes) {
        try {
            int result = myPageMapper.unLikeSelectPlans(userIdx, planIndexes);

            if (result == 0) {
                return 404;
            }
            return 200;
        } catch (Exception e) {
            log.error("e:" , e);
            return 500;
        }
    }

}
