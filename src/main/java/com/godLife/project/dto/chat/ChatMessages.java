package com.godLife.project.dto.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <strong>채팅 메시지 테이블 DTO</strong>
 * <hr/>
 * <p>{@code int chatIdx} 메시지 고유 인덱스 번호</p>
 * <p>{@code int roomIdx} 채팅방 고유 인덱스 번호</p>
 * <p>{@code int senderIdx} 발신자 고유 인덱스 번호</p>
 * <p>{@code String messageType} 메시지 타입 (TEXT / SYSTEM / IMAGE / FILE)</p>
 * <p>{@code String content} 메시지 내용</p>
 * <p>{@code String filePath} 파일인 경우 파일 경로</p>
 * <p>{@code LocalDateTime createdAt} 작성일 == 전송일</p>
 * <hr/>
 */
@Data
public class ChatMessages {
  // 메시지 고유 인덱스 번호
  private int chatIdx;
  // 채팅방 고유 인덱스 번호
  private int roomIdx;
  // 발신자 고유 인덱스 번호
  private int senderIdx;
  // 메시지 타입 (TEXT / SYSTEM / IMAGE / FILE)
  private String messageType;
  // 메시지 내용
  private String content;
  // 파일인 경우 파일 경로
  private String filePath;

  // 작성일 == 전송일
  @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
  @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt;
}
