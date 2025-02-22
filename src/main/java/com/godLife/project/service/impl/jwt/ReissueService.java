package com.godLife.project.service.impl.jwt;

import com.godLife.project.jwt.JWTUtil;
import com.godLife.project.service.jwt.RefreshService;
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
      return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
    }

    // 2. refresh 토큰 만료 여부 확인
    try {
      jwtUtil.isExpired(refresh);
    } catch (ExpiredJwtException e) {
      return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
    }

    // 3. refresh 토큰 검증
    if (!"refresh".equals(jwtUtil.getCategory(refresh))) {
      return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
    }

    //DB에 저장되어 있는지 확인
    Boolean isExist = refreshService.existsByRefresh(refresh);
    if (!isExist) {

      //response body
      return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
    }

    String username = jwtUtil.getUsername(refresh);
    String role = jwtUtil.getRole(refresh);

    // 4. 새로운 access 토큰 생성
    String newAccess = jwtUtil.createJwt("access", username, role, 600000L);
    String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

    //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
    refreshService.deleteByRefresh(refresh);
    refreshService.addRefreshToken(username, newRefresh, 86400000L);

    // 5. 응답 헤더에 새로운 access 토큰 추가
    response.setHeader("access", newAccess);
    response.addCookie(createCookie("refresh", newRefresh));

    return new ResponseEntity<>(HttpStatus.OK);
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

  private Cookie createCookie(String key, String value) {

    Cookie cookie = new Cookie(key, value);
    cookie.setMaxAge(24*60*60);
    //cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setHttpOnly(true);

    return cookie;
  }
}



