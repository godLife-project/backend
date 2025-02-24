package com.godLife.project.service.impl.jwtImpl;

import com.godLife.project.jwt.JWTUtil;
import com.godLife.project.service.jwtInterface.RefreshService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ReissueService {
  private final JWTUtil jwtUtil;

  private final RefreshService refreshService;

  public ReissueService(JWTUtil jwtUtil, RefreshService refreshService) {
    this.jwtUtil = jwtUtil;
    this.refreshService = refreshService;
  }

  public ResponseEntity<?> reissueToken(HttpServletRequest request, HttpServletResponse response) {
    // 1. 쿠키에서 refresh 토큰 가져오기
    String refresh = getRefreshTokenFromCookies(request);
    if (refresh == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(createErrorResponse("Refresh token is missing", HttpStatus.BAD_REQUEST.value()));
    }

    // 2. refresh 토큰 만료 여부 확인
    try {
      jwtUtil.isExpired(refresh);
    } catch (ExpiredJwtException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(createErrorResponse("Refresh token is expired", HttpStatus.UNAUTHORIZED.value()));
    }

    // 3. refresh 토큰 검증
    if (!"refresh".equals(jwtUtil.getCategory(refresh))) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(createErrorResponse("Invalid refresh token", HttpStatus.FORBIDDEN.value()));
    }

    //DB에 저장되어 있는지 확인
    Boolean isExist = refreshService.existsByRefresh(refresh);
    if (!isExist) {

      //response body
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(createErrorResponse("Refresh token not found in database", HttpStatus.UNAUTHORIZED.value()));
    }

    String username = jwtUtil.getUsername(refresh);
    String role = jwtUtil.getRole(refresh);

    Long accessExp = 600000L;     // 10분
    Long refreshExp = 86400000L;  // 24시간

    // 4. 새로운 access 토큰 생성
    String newAccess = jwtUtil.createJwt("access", username, role, accessExp);
    String newRefresh = jwtUtil.createJwt("refresh", username, role, refreshExp);

    //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
    refreshService.deleteByRefresh(refresh);
    refreshService.addRefreshToken(username, newRefresh, refreshExp);

    // 5. 응답 헤더에 새로운 access 토큰 추가
    response.setHeader("Authorization", "Bearer " + newAccess);
    response.addCookie(createCookie("refresh", newRefresh, request));

    return  ResponseEntity.ok().body(createSuccessResponse("Token reissued successfully"));
  }

  // 쿠키에서 refresh 토큰 가져오는 메서드
  private String getRefreshTokenFromCookies(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) return null;

    for (Cookie cookie : cookies) {
      if ("refresh".equals(cookie.getName())) {
        return cookie.getValue();
      }
    }
    return null;
  }

  private Cookie createCookie(String key, String value,  HttpServletRequest request) {

    Cookie cookie = new Cookie(key, value);
    cookie.setMaxAge(24*60*60);
    cookie.setPath("/");
    cookie.setHttpOnly(true);

    // 🔹 현재 요청이 HTTPS인지 확인하여 Secure 적용
    if (request.isSecure()) {
      cookie.setSecure(true);
    }

    return cookie;
  }

  // 에러 응답 생성 메서드
  private ErrorResponse createErrorResponse(String message, int status) {
    return new ErrorResponse(status, message);
  }

  // 성공 응답 생성 메서드
  private SuccessResponse createSuccessResponse(String message) {
    return new SuccessResponse(HttpStatus.OK.value(), message);
  }

  // 에러 응답 DTO
  public static class ErrorResponse {
    private final int status;
    private final String message;

    public ErrorResponse(int status, String message) {
      this.status = status;
      this.message = message;
    }

    public int getStatus() {
      return status;
    }

    public String getMessage() {
      return message;
    }
  }

  // 성공 응답 DTO
  public static class SuccessResponse {
    private final int status;
    private final String message;

    public SuccessResponse(int status, String message) {
      this.status = status;
      this.message = message;
    }

    public int getStatus() {
      return status;
    }

    public String getMessage() {
      return message;
    }
  }
}



