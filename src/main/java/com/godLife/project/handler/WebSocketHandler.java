package com.godLife.project.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.godLife.project.dto.chat.ChatMessage;
import com.godLife.project.dto.chat.ChatRoom;
import com.godLife.project.service.interfaces.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {
  private final ObjectMapper objectMapper;
  private final ChatService chatService;

  public void afterConnectionEstablished(WebSocketSession session) throws Exception {

  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    String payload = message.getPayload();
    ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);

    ChatRoom room = chatService.findRoomById(chatMessage.getRoomId());

    if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
      // 사용자 역할 구분 ("USER" or "ADMIN")
      String role = chatMessage.getSenderRole();

      // 세션 저장
      if ("USER".equalsIgnoreCase(role)) {
        room.setUserSession(session);
      } else if ("ADMIN".equalsIgnoreCase(role)) {
        room.setAdminSession(session);
      }

      chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다.");
      sendToEachSocket(room, new TextMessage(objectMapper.writeValueAsString(chatMessage)));

    } else if (chatMessage.getType().equals(ChatMessage.MessageType.QUIT)) {

      // 연결 끊기 처리
      if (session.equals(room.getUserSession())) {
        room.setUserSession(null);
      } else if (session.equals(room.getAdminSession())) {
        room.setAdminSession(null);
      }

      chatMessage.setMessage(chatMessage.getSender() + "님이 퇴장했습니다.");
      sendToEachSocket(room, new TextMessage(objectMapper.writeValueAsString(chatMessage)));

    } else {
      // 일반 메시지일 경우
      sendToEachSocket(room, message);
    }
  }


  private void sendToEachSocket(ChatRoom room, TextMessage message) {
    try {
      if (room.getUserSession() != null && room.getUserSession().isOpen()) {
        room.getUserSession().sendMessage(message);
      }
      if (room.getAdminSession() != null && room.getAdminSession().isOpen()) {
        room.getAdminSession().sendMessage(message);
      }
    } catch (IOException e) {
      throw new RuntimeException("메시지 전송 실패", e);
    }
  }



  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    //javascript에서  session.close해서 연결 끊음. 그리고 이 메소드 실행.
    //session은 연결 끊긴 session을 매개변수로 이거갖고 뭐 하세요.... 하고 제공해주는 것 뿐
  }

}

