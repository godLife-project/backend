package com.godLife.project.handler;

import com.godLife.project.dto.error.ErrorResponse;
import com.godLife.project.dto.list.QnaListDTO;
import com.godLife.project.exception.CustomException;
import com.godLife.project.jwt.JWTUtil;
import com.godLife.project.mapper.VerifyMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final JWTUtil jwtUtil;
  private final VerifyMapper verifyMapper;

  // 유효성 검사 에러 처리
  public Map<String, Object> getValidationErrors(BindingResult result) {
    Map<String, Object> errors = new HashMap<>();
    result.getFieldErrors().forEach(fieldError ->
        errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
    return errors;
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

  // 커스텀 예외 처리 메소드
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, HttpServletRequest request) {
    String path = ex.getPath() != null ? ex.getPath() : request.getRequestURI();

    ErrorResponse error = new ErrorResponse(
        LocalDateTime.now(),
        ex.getStatus().value(),
        ex.getStatus().getReasonPhrase(),
        ex.getMessage(),
        path // 요청 URI
    );
    return new ResponseEntity<>(error, ex.getStatus());
  }

  // 전역 예외 처리 메소드
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneralException(Exception e, HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
        "서버 내부 오류가 발생했습니다.",
        request.getRequestURI() // 요청 경로
    );
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }


  // HTTP 상태 코드 반환
  public HttpStatus getHttpStatus(int result) {
    return switch (result) {
      case 200 -> HttpStatus.OK;
      case 201 -> HttpStatus.CREATED;
      case 204 -> HttpStatus.NO_CONTENT;
      case 403 -> HttpStatus.FORBIDDEN;
      case 404 -> HttpStatus.NOT_FOUND;
      case 409 -> HttpStatus.CONFLICT;
      case 410 -> HttpStatus.GONE;
      case 412 -> HttpStatus.PRECONDITION_FAILED;
      case 422 -> HttpStatus.UNPROCESSABLE_ENTITY;
      case 500 -> HttpStatus.INTERNAL_SERVER_ERROR;
      default -> HttpStatus.BAD_REQUEST;
    };
  }

  // 응답 메시지 생성
  public Map<String, Object> createResponse(int result, Object msg) {
    Map<String, Object> message = new HashMap<>();
    message.put("status", (result == 200 || result == 201 || result == 204) ? "success" : "error");
    message.put("code", result);

    switch (result) {
      case 200 -> message.put("message", msg);
      case 201 -> message.put("message", msg);
      case 204 -> message.put("message", msg);
      case 403 -> message.put("message", msg);
      case 404 -> message.put("message", msg);
      case 409 -> message.put("message", msg);
      case 410 -> message.put("message", msg);
      case 412 -> message.put("message", msg);
      case 422 -> message.put("message", msg);
      case 500 -> message.put("message", msg);
      default -> message.put("message", msg);
    }
    return message;
  }

  // 토큰에서 user_idx 조회 하는 로직
  public int getUserIdxFromToken(String authHeader) {
    String token = authHeader.replace("Bearer ", "");
    String username = jwtUtil.getUsername(token);

    return verifyMapper.getUserIdxByUserId(username);
  }

  // 토큰에서 user_name 조회 하는 로직
  public String getUserNameFromToken(String authHeader) {
    String token = authHeader.replace("Bearer ", "");

    return jwtUtil.getUsername(token);
  }

  public Map<String, Object> createResponseWithData(int code, String message, Object data) {
    Map<String, Object> response = new HashMap<>();
    response.put("code", code);
    response.put("message", message);
    response.put("data", data);
    return response;
  }
}
