package com.godLife.project.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry){
                registry.addMapping("/api/**") //모든 API 경로에 적용
                        .allowedOrigins("http://localhost:3000") // React 주소 허용
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowCredentials(true); // 쿠키 포함 시 필요
                        /*
                        .allowedHeaders("*") // 모든 헤더 허용
                        .exposedHeaders("*") // 클라이언트가 응답 헤더를 읽을 수 있도록 허용
                        .maxAge(3600); // 1시간 동안 CORS 설정 캐싱
                         */
            }
        };
    }
}
