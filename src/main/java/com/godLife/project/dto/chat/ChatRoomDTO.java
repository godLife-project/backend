package com.godLife.project.dto.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <strong>채팅방 테이블 DTO</strong>
 * <hr/>
 * <p>{@code (int) roomIdx} 채팅방 고유 인덱스 번호</p>
 * <p>{@code (String) name} 채팅방 제목</p>
 * <p>{@code (int) isPublic} 채팅방 공개 여부</p>
 * <p>{@code (LocalDateTime) createdAt} 채팅방 생성일</p>
 * <p>{@code (int) createdBy} 채팅방 개설자 인덱스 번호</p>
 * <p>{@code (String) isDeleted} 채팅방 삭제 여부 ('N' / 'Y')</p>
 * <p>{@code (LocalDateTime) deletedAt} 채팅방 삭제일</p>
 * <hr/>
 */
@Data
public class ChatRoomDTO {
  // 채팅방 고유 인덱스 번호
  private int roomIdx;
  // 채팅방 제목
  private String name;
  // 채팅방 공개 여부
  private int isPublic;
  // 채팅방 생성일
  @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
  @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt;

  // 채팅방 개설자 인덱스 번호
  private int createdBy;
  // 채팅방 삭제 여부 ('N' / 'Y')
  private String isDeleted;

  // 채팅방 삭제일
  @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
  @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime deletedAt;
}
