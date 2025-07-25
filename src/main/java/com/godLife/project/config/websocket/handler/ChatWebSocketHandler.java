package com.godLife.project.config.websocket.handler;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {

  // WebSocket Session들을 관리하는 리스트
  private static final ConcurrentHashMap<String, WebSocketSession> clientSession = new ConcurrentHashMap<>();

  /// 웹소켓 연결 성공 할 경우 동작
  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    log.info("[+] afterConnectionEstablished :: {}", session.getId());
    clientSession.put(session.getId(), session);
  }

  /// 메세지 전송 시 구독중인 채팅방에 메시지 전달
  @Override
  protected void handleTextMessage(@NonNull WebSocketSession session,  TextMessage message) throws Exception {
    log.info("[+] handleTextMessage :: {}", session);
    log.info("[+] handleTextMessage :: {}", message.getPayload());

    clientSession.forEach((key, value) -> {
      System.out.println("key :: " + key + "  value :: " + value);
      if (!key.equals(session.getId())) {  //같은 아이디가 아니면 메시지를 전달합니다.
        try {
          value.sendMessage(message);
        } catch (IOException e) {
          log.error("[!] handleTextMessage ::{}", String.valueOf(e));
        }
      }
    });
  }

  @Override
  public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws IOException {
    clientSession.remove(session.getId());
    log.info("[+] afterConnectionClosed - Session: {}, CloseStatus: {}", session.getId(), status);
  }
}
