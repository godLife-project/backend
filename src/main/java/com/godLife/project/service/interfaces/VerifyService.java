package com.godLife.project.service.interfaces;

import com.godLife.project.dto.request.VerifyRequestDTO;

public interface VerifyService {
  // 활동 인증
  int verifyActivity(VerifyRequestDTO verifyRequestDTO);

  // 코드 생성 및 이메일 전송
  void sendCodeToEmail(String toEmail);
  // 인증 코드 검증
  boolean verifiedAuthCode(String email, String authCode);
}
