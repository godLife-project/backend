package com.godLife.project.service.impl;


import com.godLife.project.dto.datas.UserDTO;
import com.godLife.project.dto.request.GetNameNEmail;
import com.godLife.project.dto.request.myPage.GetUserPwRequestDTO;
import com.godLife.project.mapper.UserMapper;
import com.godLife.project.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

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

    // 유저 인덱스로 아이디 찾기
    @Override
    public String getUserIdByUserIdx(int userIdx) { return userMapper.getUserIdByUserIdx(userIdx); }

    // 아이디 찾기
    @Override
    public String FindUserIdByNameNEmail(GetNameNEmail getNameNEmail, boolean isMasked) {
        String tempId = userMapper.getUserId(getNameNEmail);

        if (tempId == null || tempId.isBlank()) {
            return null;
        }
        if (isMasked) { // true 면 마스킹 처리
            return doMask(tempId);
        }
        // false 면 그대로
        return tempId;
    }
    // 길이에 따른 마스킹 처리 함수
    private static String doMask(String str) {
        if (str == null || str.isBlank()) {
            throw new IllegalArgumentException("입력값이 유효하지 않습니다.");
        }

        if (str.length() > 12) {
            return masking(str, 4);
        }
        if (str.length() > 6) {
            return masking(str, 3);
        }
        return masking(str, 2);
    }
    // 입력값에 따라 마스킹 해주는 함수
    private static String masking(String str, int visible) {
        String visiblePart = str.substring(0, visible);
        String maskedPart = "*".repeat(str.length() - visible);
        return visiblePart + maskedPart;
    }


    // 비번 찾기
    @Override
    public int FindUserPw(GetUserPwRequestDTO userPwRequestDTO, String userEmail) {
        String userPw = userPwRequestDTO.getUserPw();
        String userPwConfirm = userPwRequestDTO.getUserPwConfirm();

        if (userPwConfirm == null || userPwConfirm.isBlank()) { return 400; } // 비밀번호 확인 존재 여부
        if (!userPw.equals(userPwConfirm)) { return 422; } // 변경 비밀번호와 비밀번호 확인 일치 여부 확인
        if (!userMapper.checkUserEmailExist(userEmail)) { return 404;} // 이메일 등록 여부 확인

        try {
            String encryptedPassword = passwordEncoder.encode(userPw);

            int result = userMapper.findUserPw(encryptedPassword, userEmail);
            if (result == 0) {
                return 404;
            }

            return 200;
        } catch (Exception e) {
            log.error("e: ", e);
            return 500;
        }
    }

}
