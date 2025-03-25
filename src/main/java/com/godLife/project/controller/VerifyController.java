package com.godLife.project.controller;

import com.godLife.project.dto.request.VerifyRequestDTO;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.service.interfaces.VerifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/verify")
public class VerifyController {

  @Autowired
  private GlobalExceptionHandler handler;

  private final VerifyService verifyService;

  @PostMapping("/auth/routine")
  public ResponseEntity<Map<String, Object>> verifyRoutine(@RequestHeader("Authorization") String authHeader,
                                                           @RequestBody VerifyRequestDTO verifyRequestDTO) {
    // userIdx 조회
    int userIdx = handler.getUsernameFromToken(authHeader);
    verifyRequestDTO.setUserIdx(userIdx);

    int result = verifyService.verifyActivity(verifyRequestDTO);

    // 응답 메세지 세팅
    String msg = "";
    switch (result) {
      case 200 -> msg = "활동 인증이 정상적으로 처리되었습니다.";
      case 403 -> msg = "작성자가 아닙니다. 재로그인 해주세요.";
      case 404 -> msg = "루틴 혹은 활동이 존재하지 않거나, 삭제 처리된 상태입니다.";
      case 409 -> msg = "이미 인증한 활동입니다.";
      case 410 -> msg = "회원 탈퇴한 계정입니다.";
      case 412 -> msg = "활성화 된 루틴이 아닙니다. 루틴 활성화 후 실행해 주세요.";
      case 500 -> msg = "서버 내부적으로 오류가 발생하여 요청을 수행하지 못했습니다.";
      default -> msg = "알 수 없는 오류가 발생했습니다.";
    }

    // 응답 메시지 설정
    return ResponseEntity.status(handler.getHttpStatus(result)).body(handler.createResponse(result, msg));
  }
}
