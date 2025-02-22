package com.godLife.project.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.godLife.project.dto.datas.UserDTO;
import com.godLife.project.dto.jwt.RefreshDTO;
import com.godLife.project.service.jwt.RefreshService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;

  private final JWTUtil jwtUtil;

  private final RefreshService refreshService;

  public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RefreshService refreshService) {

    super.setFilterProcessesUrl("/api/user/login");
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
    this.refreshService = refreshService;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {


    // JSON 형식으로 데이터 받기
        try {
            // 요청 본문에서 JSON 데이터를 읽어 LoginDTO 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            UserDTO loginDTO = objectMapper.readValue(request.getInputStream(), UserDTO.class);

            String username = loginDTO.getUserId();
            String password = loginDTO.getUserPw();

            // 스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new AuthenticationException("Failed to parse JSON request") {};
        }

    /*
    //클라이언트 요청에서 username, password 추출
    String username = obtainUsername(request);
    String password = obtainPassword(request);

//    System.out.println("username :: " + username);
//    System.out.println("password :: " +password);

    //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

    //token에 담은 검증을 위한 AuthenticationManager로 전달
    return authenticationManager.authenticate(authToken);

     */
  }

  //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

//    CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
//
//    String username = customUserDetails.getUsername();
//
//    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
//    GrantedAuthority auth = iterator.next();
//
//    String role = auth.getAuthority();
//
//    String token = jwtUtil.createJwt(username, role, 60 * 60 * 10 * 1000L);
//
//    response.addHeader("Authorization", "Bearer " + token);

    //유저 정보
    String username = authentication.getName();

    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
    GrantedAuthority auth = iterator.next();
    String role = auth.getAuthority();

    //토큰 생성
    String access = jwtUtil.createJwt("access", username, role, 600000L);     // 10분
    String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L); // 24시간

    // Refresh 토큰 저장
    refreshService.addRefreshToken(username, refresh, 86400000L);

    //응답 설정
    response.setHeader("access", access);
    response.addCookie(createCookie("refresh", refresh));
    response.setStatus(HttpStatus.OK.value());

  }

  //로그인 실패시 실행하는 메소드
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

    response.setStatus(401);
  }

  private Cookie createCookie(String key, String value) {

    Cookie cookie = new Cookie(key, value);
    cookie.setMaxAge(24*60*60); // 생명 주기 : 24시간
    //cookie.setSecure(true);  // https 사용시
    cookie.setPath("/");     // 쿠키 적용 범위
    cookie.setHttpOnly(true);

    return cookie;
  }
}
