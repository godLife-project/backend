package com.godLife.project.jwt;

import com.godLife.project.exception.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class ReportCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public ReportCheckFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        // 토큰이 유효하면 로그 출력
        // log.info("ReportCheckFilter 실행됨. 토큰: {}", token);

        if (token != null) {
            try {
                jwtUtil.validateToken(token);

                int isBanned = jwtUtil.getIsBanned(token);
                if (isBanned == 1) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("{\"error\": \"신고 누적으로 인해 서비스 이용이 제한되었습니다.\"}");
                    return;
                }
            } catch (UnauthorizedException e) {
                // 유효하지 않은 토큰이면 여기서 처리 (예: 401 응답)
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
       //  log.info("Authorization 헤더: {}", bearer);
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
