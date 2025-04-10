package com.godLife.project.dto.contents.temp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QnaContentDTO_temp {
  @Schema(description = "질문 컨텐츠 인덱스", example = "1")
  private Long qContentIdx;

  @Schema(description = "질문 인덱스", example = "1")
  private Integer questionIdx;

  @Schema(description = "응답자 인덱스", example = "1")
  private Long responseIdx;

  @Schema(description = "응답 내용 (답변 및 추가질문", example = "답변입니다.")
  private String responseSub;

  @Schema(description = "응답 시간", example = "2025-02-16 HH:mm:ss")
  @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
  @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
  private LocalDateTime createdAt;


}
