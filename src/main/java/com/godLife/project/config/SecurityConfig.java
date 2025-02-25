package com.godLife.project.config;

import com.godLife.project.jwt.CustomLogoutFilter;
import com.godLife.project.jwt.JWTFilter;
import com.godLife.project.jwt.JWTUtil;
import com.godLife.project.jwt.LoginFilter;
import com.godLife.project.service.jwtInterface.RefreshService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
  private final AuthenticationConfiguration authenticationConfiguration;

  private final JWTUtil jwtUtil;

  private final RefreshService refreshService;

  public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil, RefreshService refreshService) {

    this.authenticationConfiguration = authenticationConfiguration;
    this.jwtUtil = jwtUtil;
    this.refreshService = refreshService;
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
        // 스웨거 관련
        .requestMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**", "/favicon.ico")
        .permitAll()
        // 카테고리 관련
        .requestMatchers("/api/categories/*").permitAll()
        // 추가 경로 제외
        .requestMatchers("/", "/api/user/join", "/api/user/checkId/*", "/api/test1").permitAll()
        // refresh 토큰 검증 api경로
        .requestMatchers("/api/reissue").permitAll()
        // 특정 권한만 접근 가능
        .requestMatchers("/api/categories/auth/authority").hasAnyAuthority("2", "3", "4", "5", "6", "7")
        .requestMatchers("/admin").hasAuthority("7")
        .anyRequest().authenticated()
    );

    // 필터 적용
    http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);
    http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshService), UsernamePasswordAuthenticationFilter.class);
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

            configuration.setAllowedOrigins(List.of("http://localhost:3000", "https://4214-175-117-30-43.ngrok-free.app"));
            configuration.setAllowedMethods(Collections.singletonList("*"));
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
