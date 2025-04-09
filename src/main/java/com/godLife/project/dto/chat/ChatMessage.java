package com.godLife.project.dto.chat;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessage {
  public enum MessageType {
    ENTER, TALK, QUIT // QUIT으로 통일 (EXIT보다 명확)
  }

  private MessageType type;
  private String roomId;
  private String sender;
  private String senderRole;      // "USER" 또는 "ADMIN"
  private String message;
  private LocalDateTime time = LocalDateTime.now(); // 기본값 설정
}
