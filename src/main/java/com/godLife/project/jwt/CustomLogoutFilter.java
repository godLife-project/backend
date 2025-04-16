package com.godLife.project.jwt;

import com.godLife.project.service.interfaces.adminInterface.ServiceAdminService;
import com.godLife.project.service.interfaces.jwtInterface.RefreshService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

  private final JWTUtil jwtUtil;
  private final RefreshService refreshService;
  private final ServiceAdminService serviceAdminService;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
  }

  private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    //path and method verify
    String requestUri = request.getRequestURI();
    if (!"/api/user/logout".equals(requestUri)) {  // 로그아웃 경로 지정

      filterChain.doFilter(request, response);
      return;
    }
    String requestMethod = request.getMethod();
    if (!requestMethod.equals("POST")) {

      filterChain.doFilter(request, response);
      return;
    }

    //get refresh token
    String refresh = getRefreshTokenFromCookies(request);

    //refresh null check
    if (refresh == null) {

      sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Refresh 토큰이 쿠키에 없습니다.");
      log.warn("logoutFilter - NoRefresh :: 쿠키에 Refresh 토큰이 없습니다.");
      return;
    }

    //expired check
    try {
      jwtUtil.isExpired(refresh);
    } catch (ExpiredJwtException e) {

      //response status code
      sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Refresh 토큰이 만료되었습니다.");
      log.warn("logoutFilter - isExpired :: Refresh 토큰이 이미 만료 되었습니다.");
      return;
    }

    // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
    String category = jwtUtil.getCategory(refresh);
    if (!category.equals("refresh")) {

      //response status code
      sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "유효하지 않은 Refresh 토큰입니다.");
      log.warn("logoutFilter - isValid :: Refresh 토큰이 유효하지 않습니다.");
      return;
    }

    //DB에 저장되어 있는지 확인
    Boolean isExist = refreshService.existsByRefresh(refresh);
    if (!isExist) {

      //response status code
      sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "해당 Refresh 토큰이 DB에 존재하지 않습니다.");
      log.warn("logoutFilter - NoDatabase :: DB에 Refresh 토큰이 없습니다.");
      return;
    }

    //로그아웃 진행
    serviceAdminService.setCenterLogoutByAdmin3467(refresh);
    //Refresh 토큰 DB에서 제거
    refreshService.deleteByRefresh(refresh);

    //Refresh 토큰 Cookie 값 0
    Cookie cookie = new Cookie("refresh", null);
    cookie.setMaxAge(0);
    cookie.setPath("/");
    response.addCookie(cookie);

    // 성공 응답
    log.info("logoutFilter - NoDatabase :: refresh 토큰 삭제,, 로그아웃완료");

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    PrintWriter writer = response.getWriter();
    writer.write("{\"message\": \"로그아웃이 완료되었습니다.\"}");
    writer.flush();
  }

  private String getRefreshTokenFromCookies(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null)  {
      log.warn("logoutFilter - NoDatabase :: 쿠키 없음");
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

  // 에러 응답을 JSON 형식으로 보내는 메서드
  private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
    response.setStatus(status);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    PrintWriter writer = response.getWriter();
    writer.write("{\"error\": \"" + message + "\"}");
    writer.flush();
  }
}


