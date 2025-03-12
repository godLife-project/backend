package com.godLife.project.service.interfaces;

import com.godLife.project.dto.request.VerifyRequestDTO;

public interface VerifyService {
  // 활동 인증
  int verifyActivity(VerifyRequestDTO verifyRequestDTO);
}
