package com.godLife.project.handler.websocket;

import com.godLife.project.exception.UnauthorizedException;
import com.godLife.project.jwt.JWTUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {

  private final JWTUtil jwtUtil;

  @Override
  public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
    final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

    // websocket 연결 시 헤더의 access 토큰 검증
    if (StompCommand.CONNECT == accessor.getCommand()) {
      final String authorization = jwtUtil.extractJwt(accessor).replace("Bearer ", "");

      // 토큰 유효 검증
      jwtUtil.validateToken(authorization);

      String role = jwtUtil.getRole(authorization);
      List<String> allowedRoles = Arrays.asList("3", "4", "6", "7");

      if (!allowedRoles.contains(role)) {
        throw UnauthorizedException.of("ForbiddenRole", "접속이 허용되지 않은 권한입니다.");
      }

//      String userName = jwtUtil.getUsername(authorization);
//      System.out.println(userName);
//      accessor.setUser(new UsernamePasswordAuthenticationToken(userName, null, List.of()));
//      System.out.println(">>> setUser 완료: " + accessor.getUser());
    }
    return message;
  }
}
