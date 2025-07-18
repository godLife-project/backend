package com.godLife.project.dto.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <strong>채팅방 참가자 테이블 DTO</strong>
 * <hr/>
 * <p>{@code int roomIdx} 채팅방 고유 인덱스 번호</p>
 * <p>{@code int adminIdx} 참가자 고유 인덱스 번호</p>
 * <p>{@code LocalDateTime joinedAt} 채팅방 참여일</p>
 * <p>{@code LocalDateTime leftAt} 채팅방 퇴장일</p>
 * <p>{@code int lastReadIdx} 마지막으로 읽은 메시지 인덱스 번호</p>
 * <hr/>
 */
@Data
public class ChatMembers {
  // 채팅방 고유 인덱스 번호
  private int roomIdx;
  // 참가자 고유 인덱스 번호
  private int adminIdx;

  // 채팅방 참가일
  @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
  @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime joinedAt;

  // 채팅방 퇴장일
  @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
  @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime leftAt;

  // 마지막으로 읽은 메시지 인덱스 번호
  private int lastReadIdx;

}
