package com.godLife.project.controller;


import com.godLife.project.dto.request.MyPageRequestDTO;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.service.interfaces.MyPageService;
import com.godLife.project.service.interfaces.jwtInterface.RefreshService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/myPage/auth")
@RequiredArgsConstructor
public class MyPageController {

  private final GlobalExceptionHandler handler;

  private final MyPageService myPageService;

  private final RefreshService refreshService;


  @PatchMapping("/accountDeletion")
  public ResponseEntity<Map<String, Object>> accountDeletion(@RequestBody MyPageRequestDTO myPageRequestDTO,
                                                             @RequestHeader("Authorization") String authHeader,
                                                             HttpServletRequest request, HttpServletResponse response) {
    int userIdx = handler.getUsernameFromToken(authHeader);
    String userPw = myPageRequestDTO.getUserPw();

    int result = myPageService.deleteAccount(userIdx, userPw);

    // 응답 메세지 세팅
    String msg = "";
    switch (result) {
      case 200 -> msg = "회원 탈퇴 완료";
      case 403 -> msg = "비밀번호가 틀렸습니다.";
      case 404 -> msg = "이미 탈퇴했거나 없는 계정입니다.";
      case 500 -> msg = "서버 내부적으로 오류가 발생하여 요청을 수행하지 못했습니다.";
      default -> msg = "알 수 없는 오류가 발생했습니다.";
    }

    String refresh = getRefreshTokenFromCookies(request);

    //로그아웃 진행
    //Refresh 토큰 DB에서 제거
    refreshService.deleteByRefresh(refresh);

    //Refresh 토큰 Cookie 값 0
    Cookie cookie = new Cookie("refresh", null);
    cookie.setMaxAge(0);
    cookie.setPath("/");
    response.addCookie(cookie);

    // 응답 메시지 설정
    return ResponseEntity.status(handler.getHttpStatus(result)).body(handler.createResponse(result, msg));
  }

  @PatchMapping("/accountDeletion/cancel")
  public ResponseEntity<Map<String, Object>> accountDeletionCancel(@RequestBody MyPageRequestDTO myPageRequestDTO,
                                                             @RequestHeader("Authorization") String authHeader) {
    int userIdx = handler.getUsernameFromToken(authHeader);
    String userPw = myPageRequestDTO.getUserPw();

    int result = myPageService.deleteCancelAccount(userIdx, userPw);

    // 응답 메세지 세팅
    String msg = "";
    switch (result) {
      case 200 -> msg = "탈퇴 취소 완료";
      case 403 -> msg = "비밀번호가 틀렸습니다.";
      case 404 -> msg = "이미 완전히 삭제 되었거나 아직 탈퇴하지 않은 계정입니다.";
      case 500 -> msg = "서버 내부적으로 오류가 발생하여 요청을 수행하지 못했습니다.";
      default -> msg = "알 수 없는 오류가 발생했습니다.";
    }

    // 응답 메시지 설정
    return ResponseEntity.status(handler.getHttpStatus(result)).body(handler.createResponse(result, msg));
  }

  private String getRefreshTokenFromCookies(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null)  {
      System.out.println("쿠키 없음");
      return null;
    }

    for (Cookie cookie : cookies) {
      //System.out.println(cookie.getName());
      if ("refresh".equals(cookie.getName())) {
        return cookie.getValue();
      }
    }
    return null;
  }

}
