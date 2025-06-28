package com.godLife.project.dto.chat;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * <strong>읽은 메시지 추적용 테이블 DTO</strong>
 * <p>{@code int chatIdx} 메시지 고유 인덱스 번호</p>
 * <p>{@code int adminIdx} 메시지 읽은 유저의 고유 인덱스 번호</p>
 * <p>{@code LocalDateTime readAt} 조회일</p>
 */
@Data
public class ChatRead {
  // 메시지 고유 인덱스 번호
  private int chatIdx;
  // 메시지 읽은 유저의 고유 인덱스 번호
  private int adminIdx;
  // 조회일
  private LocalDateTime readAt;
}
