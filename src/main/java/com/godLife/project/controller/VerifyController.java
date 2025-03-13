package com.godLife.project.controller;

import com.godLife.project.dto.request.VerifyRequestDTO;
import com.godLife.project.service.interfaces.VerifyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/verify")
public class VerifyController {

  private final VerifyService verifyService;

  public VerifyController (VerifyService verifyService) { this.verifyService = verifyService; }

  @PostMapping("/auth/routine")
  public ResponseEntity<Map<String, Object>> verifyRoutine(@RequestBody VerifyRequestDTO verifyRequestDTO) {
    int result = verifyService.verifyActivity(verifyRequestDTO);

    // 응답 메세지 세팅
    String msg = "";
    switch (result) {
      case 200 -> msg = "활동 인증이 정상적으로 처리되었습니다.";
      case 403 -> msg = "작성자가 아닙니다. 재로그인 해주세요.";
      case 404 -> msg = "루틴 혹은 활동이 존재하지 않거나, 삭제 처리된 상태입니다.";
      case 409 -> msg = "이미 인증한 활동입니다.";
      case 412 -> msg = "활성화 된 루틴이 아닙니다. 루틴 활성화 후 실행해 주세요.";
      case 500 -> msg = "서버 내부적으로 오류가 발생하여 루틴을 저장하지 못했습니다.";
      default -> msg = "알 수 없는 오류가 발생했습니다.";
    }

    // 응답 메시지 설정
    return ResponseEntity.status(getHttpStatus(result)).body(createResponse(result, msg));
  }



  // JSON 파싱 오류 처리 메소드
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, Object>> handleJsonParseException(HttpMessageNotReadableException ex) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("status", "error");
    errorResponse.put("message", "잘못된 JSON 형식입니다. 필드값이 누락되었거나, 필드명의 오타가 있을 수 있습니다.");
    errorResponse.put("code", 400);

    return ResponseEntity.badRequest().body(errorResponse);
  }

  // HTTP 상태 코드 반환
  private HttpStatus getHttpStatus(int result) {
    return switch (result) {
      case 200 -> HttpStatus.OK;
      case 201 -> HttpStatus.CREATED;
      case 403 -> HttpStatus.FORBIDDEN;
      case 404 -> HttpStatus.NOT_FOUND;
      case 409 -> HttpStatus.CONFLICT;
      case 412 -> HttpStatus.PRECONDITION_FAILED;
      case 500 -> HttpStatus.INTERNAL_SERVER_ERROR;
      default -> HttpStatus.BAD_REQUEST;
    };
  }

  // 응답 메시지 생성
  private Map<String, Object> createResponse(int result, Object msg) {
    Map<String, Object> message = new HashMap<>();
    message.put("status", (result == 200 || result == 201) ? "success" : "error");
    message.put("code", result);

    switch (result) {
      case 200 -> message.put("message", msg);
      case 201 -> message.put("message", msg);
      case 403 -> message.put("message", msg);
      case 404 -> message.put("message", msg);
      case 409 -> message.put("message", msg);
      case 412 -> message.put("message", msg);
      case 500 -> message.put("message", msg);
      default -> message.put("message", msg);
    }
    return message;
  }
}
