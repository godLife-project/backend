package com.godLife.project.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.godLife.project.dto.datas.UserDTO;
import com.godLife.project.dto.jwtDTO.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;


@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private static final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

  @Override
  public void handle(HttpServletRequest request,
                     HttpServletResponse response,
                     AccessDeniedException accessDeniedException) throws IOException {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.isAuthenticated()) {
      Object principal = authentication.getPrincipal();

      if (principal instanceof CustomUserDetails customUserDetails) {

        logger.warn("접근 거부 - 사용자 ID: {}, URI: {}, 권한 인덱스: {}",
            customUserDetails.getUsername(),
            request.getRequestURI(),
            customUserDetails.getAuthorityIdx()
        );
      } else {
        logger.warn("접근 거부 - 인증된 사용자이나 CustomUserDetails 아님, URI: {}", request.getRequestURI());
      }
    } else {
      logger.warn("접근 거부 - 비인증 사용자, URI: {}", request.getRequestURI());
    }

    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType("application/json;charset=UTF-8");

    String json = new ObjectMapper().writeValueAsString(Map.of(
        "timestamp", LocalDateTime.now().toString(),
        "status", 403,
        "error", "Forbidden",
        "message", accessDeniedException.getMessage(),
        "path", request.getRequestURI()
    ));

    response.getWriter().write(json);
  }
}

