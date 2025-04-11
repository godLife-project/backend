package com.godLife.project.jwt;

import com.godLife.project.dto.jwtDTO.CustomUserDetails;
import com.godLife.project.dto.datas.UserDTO;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

  private final JWTUtil jwtUtil;

  public JWTFilter(JWTUtil jwtUtil) {

    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

    // 헤더에서 access키에 담긴 토큰을 꺼냄
    String authorization = request.getHeader("Authorization");

    // 토큰이 없다면 다음 필터로 넘김
    if (authorization == null || !authorization.startsWith("Bearer ")) {

      filterChain.doFilter(request, response);

      return;
    }

    //Bearer 부분 제거 후 순수 토큰만 획득
    String accessToken = authorization.split(" ")[1];

    // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
    try {
      jwtUtil.isExpired(accessToken);
    } catch (ExpiredJwtException e) {

      // JSON 형식으로 응답 보내기
      response.setContentType("application/json");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

      // JSON 데이터
      String jsonResponse = "{\"message\": \"access token expired\"}";

      // PrintWriter로 JSON 응답 출력
      PrintWriter writer = response.getWriter();
      writer.print(jsonResponse);
      writer.flush();  // 데이터를 전송

      log.error("JWT access 토큰 만료: {}", e.getMessage());
    }

    // 토큰이 access인지 확인 (발급시 페이로드에 명시)
    String category = jwtUtil.getCategory(accessToken);

    if (!category.equals("access")) {
      // JSON 형식으로 응답 보내기
      response.setContentType("application/json");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

      // JSON 데이터
      String jsonResponse = "{\"message\": \"invalid access token\"}";

      // PrintWriter로 JSON 응답 출력
      PrintWriter writer = response.getWriter();
      writer.print(jsonResponse);
      writer.flush();  // 데이터를 전송

      log.error("유효하지 않은 access 토큰 사용: {}", category);

      return;
    }

// username, role 값을 획득
    String username = jwtUtil.getUsername(accessToken);
    String role = jwtUtil.getRole(accessToken);

    UserDTO userDTO = new UserDTO();
    userDTO.setUserId(username);
    userDTO.setAuthorityIdx(Integer.parseInt(role));
    CustomUserDetails customUserDetails = new CustomUserDetails(userDTO);

    Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authToken);

    filterChain.doFilter(request, response);



//    //request에서 Authorization 헤더를 찾음
//    String authorization= request.getHeader("Authorization");
//
//    //Authorization 헤더 검증
//    if (authorization == null || !authorization.startsWith("Bearer ")) {
//
//      System.out.println("token null");
//      filterChain.doFilter(request, response);
//
//      //조건이 해당되면 메소드 종료 (필수)
//      return;
//    }
//
//    System.out.println("authorization now");
//    //Bearer 부분 제거 후 순수 토큰만 획득
//    String token = authorization.split(" ")[1];
//
//    //토큰 소멸 시간 검증
//    if (jwtUtil.isExpired(token)) {
//
//      System.out.println("token expired");
//      filterChain.doFilter(request, response);
//
//      //조건이 해당되면 메소드 종료 (필수)
//      return;
//    }
//
//    //토큰에서 username과 role 획득
//    String username = jwtUtil.getUsername(token);
//    String role = jwtUtil.getRole(token);
//
//    //System.out.println("========================================  토큰 조회  =======================================");
//    //System.out.println("username : " + username);
//    //System.out.println("role : " + role);
//    //System.out.println("============================================================================================");
//
//    //userEntity를 생성하여 값 set
//    UserDTO userDTO = new UserDTO();
//    userDTO.setUserId(username);
//    userDTO.setUserPw("temppassword");
//    userDTO.setAuthorityIdx(Integer.parseInt(role));
//
//    //System.out.println("========================================  UserDTO 확인  =======================================");
//    //System.out.println(userDTO);
//    //System.out.println("============================================================================================");
//
//    //UserDetails에 회원 정보 객체 담기
//    CustomUserDetails customUserDetails = new CustomUserDetails(userDTO);
//
//    //System.out.println("========================================  customUserDetails 확인  =======================================");
//    //System.out.println(customUserDetails.getUsername());
//    //for (GrantedAuthority authority : customUserDetails.getAuthorities()) {
//    //  System.out.println(authority.getAuthority());
//    //}
//    //System.out.println("============================================================================================");
//
//    //스프링 시큐리티 인증 토큰 생성
//    Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
//    //세션에 사용자 등록
//    SecurityContextHolder.getContext().setAuthentication(authToken);
//
//    filterChain.doFilter(request, response);

  }
}
