package com.godLife.project.listener;

import com.godLife.project.handler.redisSession.RedisSessionManager;
import com.godLife.project.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;


@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

  private final JWTUtil jwtUtil;

  private final RedisSessionManager redisSessionManager;

  // 클라이언트가 연결되었을 때 세션 정보 저장
  @EventListener
  public void handleWebSocketConnectedListener(SessionConnectedEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

    // CONNECT_ACK에서 Principal 정보는 직접적으로 안 들어있음 → simpConnectMessage에서 꺼내야 함
    Message<?> connectMessage = (Message<?>) headerAccessor.getHeader("simpConnectMessage");
    StompHeaderAccessor connectAccessor = StompHeaderAccessor.wrap(connectMessage);

    List<String> authHeaders = connectAccessor.getNativeHeader("Authorization");
    String token = (authHeaders != null && !authHeaders.isEmpty()) ? authHeaders.get(0).replace("Bearer ", "") : null;

    String userName = null;
    if (token != null) {
      userName = jwtUtil.getUsername(token);
    }
    String sessionId = connectAccessor.getSessionId();

//    System.out.println("headerAccessor --:: " + headerAccessor);
//    System.out.println("connectAccessor --:: " + connectAccessor);
//    System.out.println("userName --:: " + userName);
//    System.out.println("sessionId --:: " + sessionId);

    // 세션 ID와 유저 정보 저장
    redisSessionManager.addSession(userName, sessionId);
    log.info("유저 : {} 연결됨, 세션 ID : {}", userName, sessionId);
  }

  // 클라이언트가 연결을 끊었을 때 세션 정보 제거
  @EventListener
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

    String sessionId = headerAccessor.getSessionId();

    // 세션 ID를 기준으로 해당 유저 정보 제거
    String userName = redisSessionManager.getUsernameBySessionId(sessionId);

    if (userName != null) {
      redisSessionManager.removeSession(userName);
    }
    log.info("유저 : {} 연결 끊어짐", userName);
  }
}
