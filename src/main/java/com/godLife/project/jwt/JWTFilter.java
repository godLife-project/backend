package com.godLife.project.jwt;

import com.godLife.project.dto.CustomUserDetails;
import com.godLife.project.dto.datas.UserDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

  private final JWTUtil jwtUtil;

  public JWTFilter(JWTUtil jwtUtil) {

    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

    //request에서 Authorization 헤더를 찾음
    String authorization= request.getHeader("Authorization");

    //Authorization 헤더 검증
    if (authorization == null || !authorization.startsWith("Bearer ")) {

      System.out.println("token null");
      filterChain.doFilter(request, response);

      //조건이 해당되면 메소드 종료 (필수)
      return;
    }

    System.out.println("authorization now");
    //Bearer 부분 제거 후 순수 토큰만 획득
    String token = authorization.split(" ")[1];

    //토큰 소멸 시간 검증
    if (jwtUtil.isExpired(token)) {

      System.out.println("token expired");
      filterChain.doFilter(request, response);

      //조건이 해당되면 메소드 종료 (필수)
      return;
    }

    //토큰에서 username과 role 획득
    String username = jwtUtil.getUsername(token);
    String role = jwtUtil.getRole(token);

    //System.out.println("========================================  토큰 조회  =======================================");
    //System.out.println("username : " + username);
    //System.out.println("role : " + role);
    //System.out.println("============================================================================================");

    //userEntity를 생성하여 값 set
    UserDTO userDTO = new UserDTO();
    userDTO.setUserId(username);
    userDTO.setUserPw("temppassword");
    userDTO.setAuthorityIdx(Integer.parseInt(role));

    //System.out.println("========================================  UserDTO 확인  =======================================");
    //System.out.println(userDTO);
    //System.out.println("============================================================================================");

    //UserDetails에 회원 정보 객체 담기
    CustomUserDetails customUserDetails = new CustomUserDetails(userDTO);

    //System.out.println("========================================  customUserDetails 확인  =======================================");
    //System.out.println(customUserDetails.getUsername());
    //for (GrantedAuthority authority : customUserDetails.getAuthorities()) {
    //  System.out.println(authority.getAuthority());
    //}
    //System.out.println("============================================================================================");

    //스프링 시큐리티 인증 토큰 생성
    Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
    //세션에 사용자 등록
    SecurityContextHolder.getContext().setAuthentication(authToken);

    filterChain.doFilter(request, response);

  }
}
