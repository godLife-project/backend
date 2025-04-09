package com.godLife.project.dto.chat;

import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.io.Serializable;

@Data
public class ChatRoom implements Serializable {

  private String roomId;           // 채팅방 고유 ID (예: user_jihyun)
  private String name;             // 채팅방 이름 (예: "지현님의 문의방")

  private String userName;         // 사용자 이름 또는 ID
  private String adminName;        // 관리자 이름 또는 ID

  private WebSocketSession userSession;   // 사용자 WebSocket 세션
  private WebSocketSession adminSession;  // 관리자 WebSocket 세션

  // 생성 메서드 (정적 팩토리 패턴)
  public static ChatRoom create(String roomId, String name) {
    ChatRoom room = new ChatRoom();
    room.setRoomId(roomId);
    room.setName(name);
    return room;
  }

  // 세션 등록 편의 메서드
  public void join(String role, WebSocketSession session, String sender) {
    if ("USER".equalsIgnoreCase(role)) {
      this.userSession = session;
      this.userName = sender;
    } else if ("ADMIN".equalsIgnoreCase(role)) {
      this.adminSession = session;
      this.adminName = sender;
    }
  }

  // 세션 퇴장 처리
  public void leave(WebSocketSession session) {
    if (session.equals(this.userSession)) {
      this.userSession = null;
      this.userName = null;
    } else if (session.equals(this.adminSession)) {
      this.adminSession = null;
      this.adminName = null;
    }
  }

  // 채팅방 비었는지 확인 (필요 시 삭제 트리거용)
  public boolean isEmpty() {
    return userSession == null && adminSession == null;
  }
}
