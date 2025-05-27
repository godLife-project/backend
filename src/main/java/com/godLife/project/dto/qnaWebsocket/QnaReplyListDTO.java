package com.godLife.project.dto.qnaWebsocket;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.godLife.project.dto.contents.QnaReplyDTO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * {@code QnaReplyDTO} 와 비슷하지만, {@code qnaIdx} 가 없습니다
 * <p>답변 상세 보기 시, 답변을 큐 스택 처럼 보이도록 하기 위함입니다.</p>
 * @see QnaReplyDTO
 */
@Data
public class QnaReplyListDTO {

  private int qnaReplyIdx;

  private String userNick;

  private String nickTag;

  private int userIdx;

  private String content;

  @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
  @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt;

  @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
  @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime modifiedAt;
}
