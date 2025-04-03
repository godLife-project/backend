package com.godLife.project.controller;

import com.godLife.project.dto.datas.UserDTO;
import com.godLife.project.dto.request.GetEmailRequestDTO;
import com.godLife.project.dto.request.GetNameNEmail;
import com.godLife.project.dto.request.myPage.GetUserPwRequestDTO;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.service.impl.redis.RedisService;
import com.godLife.project.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  private final RedisService redisService;

  private final GlobalExceptionHandler handler;

  // 회원가입
  @Operation(summary = "회원가입 API", description = "유효성 검사 후 모두 통과시 정보 Insert")
  @PostMapping("/join")
  public ResponseEntity<Map<String, String>> join (@Valid @RequestBody UserDTO joinUserDTO, BindingResult result) {
    // System.out.println(joinUserDTO);
    // 유효성 검사
    if (result.hasErrors()) {
      Map<String, String> errors = new HashMap<>();
      result.getFieldErrors().forEach(fieldError -> {
        errors.put(fieldError.getField(), fieldError.getDefaultMessage());
      });

      return ResponseEntity.badRequest().body(errors);
    }
    else {
      // 데이터 삽입
      String successMessage = userService.insertUser(joinUserDTO);
      // 삽입 완료시 완료 메세지
      if ("Success".equals(successMessage)) {
        Map<String, String> success = new HashMap<>();
        success.put("message", "회원가입 완료");
        return ResponseEntity.ok(success);
      }
      // 삽입 에러시 에러 메세지
      else {
        Map<String, String> error = new HashMap<>();
        error.put("message",successMessage);
        return ResponseEntity.badRequest().body(error);
      }
    }
  }

  // 아이디 중복 체크
  @Operation(summary = "회원가입_아이디 체크 API", description = "중복 아이디 조회")
  @GetMapping("/checkId/{userId}")
  public ResponseEntity<Boolean> checkUserIdExist(@PathVariable String userId) {
    //System.out.println(userId + " : 아이디 체크 요청");
    Boolean isAvailable = userService.checkUserIdExist(userId);
    return ResponseEntity.ok(isAvailable);
  }

  // 아이디 찾기
  @GetMapping("/find/userId")
  public ResponseEntity<Map<String, Object>> findUserId(@Valid @ModelAttribute GetNameNEmail request,
                                                        BindingResult valid) {
    if (valid.hasErrors()) {
      return ResponseEntity.badRequest().body(handler.getValidationErrors(valid));
    }

    String result = userService.FindUserIdByNameNEmail(request, true);

    if (result == null || result.isBlank()) {
      return ResponseEntity.status(404).body(handler.createResponse(404, "아이디가 없습니다."));
    }
    return ResponseEntity.ok().body(handler.createResponse(200, result));
  }
  // 아이디 찾기 마스킹 제거
  @GetMapping("/find/userId/noMask")
  public ResponseEntity<Map<String, Object>> noMaskingUserId(@Valid @ModelAttribute GetNameNEmail request,
                                                             BindingResult valid) {
    if (valid.hasErrors()) {
      return ResponseEntity.badRequest().body(handler.getValidationErrors(valid));
    }

    // 이메일 인증 여부 검증
    String key = "EMAIL_VERIFIED: " + request.getUserEmail();
    String verified = redisService.getData(key); // 인증 여부 조회

    if (verified == null || !verified.equals("true")) {
      return ResponseEntity.status(handler.getHttpStatus(412))
          .body(handler.createResponse(412, "이메일 인증이 필요합니다."));
    }

    String result = userService.FindUserIdByNameNEmail(request, false);

    redisService.deleteData(key); // 인증 데이터 삭제

    if (result == null || result.isBlank()) {
      return ResponseEntity.status(404).body(handler.createResponse(404, "아이디가 없습니다."));
    }
    return ResponseEntity.ok().body(handler.createResponse(200, result));

  }

  // 비번 찾기
  @PatchMapping("/find/userPw/{userEmail}")
  public ResponseEntity<Map<String, Object>> findUserPw(@Valid @RequestBody GetUserPwRequestDTO request,
                                                        BindingResult valid,
                                                        @PathVariable String userEmail) {
    if (valid.hasErrors()) {
      return ResponseEntity.badRequest().body(handler.getValidationErrors(valid));
    }

    // 이메일 인증 여부 검증
    String key = "EMAIL_VERIFIED: " + userEmail;
    String verified = redisService.getData(key); // 인증 여부 조회

    if (verified == null || !verified.equals("true")) {
      return ResponseEntity.status(handler.getHttpStatus(412))
          .body(handler.createResponse(412, "이메일 인증이 필요합니다."));
    }

    int result = userService.FindUserPw(request, userEmail);

    // 응답 메세지 세팅
    String msg = "";
    switch (result) {
      case 200 -> msg = "비밀번호 수정 완료";
      case 400 -> msg = "비밀번호 확인 필드의 값이 누락되었습니다.";
      case 404 -> msg = "탈퇴했거나, 존재하지 않는 유저입니다.";
      case 422 -> msg = "비밀번호가 일치하지 않습니다.";
      case 500 -> msg = "서버 내부적으로 오류가 발생하여 요청을 수행하지 못했습니다.";
      default -> msg = "알 수 없는 오류가 발생했습니다.";
    }

    redisService.deleteData(key);

    // 응답 메시지 설정
    return ResponseEntity.status(handler.getHttpStatus(result)).body(handler.createResponse(result, msg));
  }


}