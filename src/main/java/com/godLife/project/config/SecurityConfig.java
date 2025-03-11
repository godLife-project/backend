package com.godLife.project.config;

import com.godLife.project.jwt.CustomLogoutFilter;
import com.godLife.project.jwt.JWTFilter;
import com.godLife.project.jwt.JWTUtil;
import com.godLife.project.jwt.LoginFilter;
import com.godLife.project.service.UserService;
import com.godLife.project.service.jwtInterface.RefreshService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Value("${cors.allowed-origins}")
  private String allowedOrigins;

  //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
  private final AuthenticationConfiguration authenticationConfiguration;

  private final JWTUtil jwtUtil;

  private final RefreshService refreshService;

  private final UserService userService;

  public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil, RefreshService refreshService, @Lazy UserService userService) {

    this.authenticationConfiguration = authenticationConfiguration;
    this.jwtUtil = jwtUtil;
    this.refreshService = refreshService;
    this.userService = userService;
  }

  //AuthenticationManager Bean 등록
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

    return configuration.getAuthenticationManager();
  }

  // 암호화 작업
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // 보안 설정
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // csrf disable
    http.csrf(AbstractHttpConfigurer::disable);
    // Form 로그인 방식 disable
    http.formLogin(AbstractHttpConfigurer::disable);
    // http basic 인증 방식 disable
    http.httpBasic(AbstractHttpConfigurer::disable);

    // 경로별 인가 작업
    http.authorizeHttpRequests(auth -> auth
    // 지정한 엔드포인트는 로그인시 접근 가능 (유저 권한)
        // 테스트 용 (유저 권한)
        .requestMatchers("/api/test/auth/**").authenticated()
        // 루틴 관련
        .requestMatchers("/api/plan/auth/**").authenticated()
        // 인증 관련
        .requestMatchers("/api/verify/auth/**").authenticated()
        // 챌린지 참여/인증
        //.requestMatchers("/api/{challIdx}/join", "/api/{challIdx}/verify").authenticated()

    // 지정한 엔드포인트는 해당 권한 등급이 없으면 로그인을 해도 접근 못함 (관리자)
        // 관리자 권한 카테고리 조회
        .requestMatchers("/api/categories/admin/**").hasAnyAuthority("2", "3", "4", "5", "6", "7")
        // 테스트 용 (관리자 권한)
        .requestMatchers("/api/admin").hasAuthority("7")
        // 관리자 권한 챌린지 작성
        .requestMatchers("/api/admin/challenge/create").hasAnyAuthority("2", "3", "4", "5", "6", "7")


    // 그 외 모든 접근 허용 (비 로그인 접근)
        .anyRequest().permitAll()
    );

    // 필터 적용
    http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);
    http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshService, userService), UsernamePasswordAuthenticationFilter.class);
    http.addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshService), LogoutFilter.class);

    // 세션 비활성화
    http.sessionManagement((session) -> session
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    // CORS 허용
    http
        .cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

          @Override
          public CorsConfiguration getCorsConfiguration(@NonNull HttpServletRequest request) {

            CorsConfiguration configuration = new CorsConfiguration();

            configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(", ")));
            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
            configuration.setAllowCredentials(true);
            configuration.setAllowedHeaders(Collections.singletonList("*"));
            configuration.setMaxAge(3600L);

            configuration.setExposedHeaders(Collections.singletonList("Authorization"));

            return configuration;
          }
        })));



        return http.build();
    }

}


