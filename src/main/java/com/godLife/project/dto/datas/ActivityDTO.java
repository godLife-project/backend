package com.godLife.project.dto.datas;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalTime;

@Data
public class ActivityDTO {
  @Schema(description = "활동 인덱스", example = "1")
  private int activityIdx;

  @Schema(description = "루틴 인덱스", example = "1")
  private int planIdx;

  @Schema(description = "활동명", example = "조깅 2시간")
  private String activityName;

  @Schema(description = "시작 시간", example = "08:00:00")
  @JsonSerialize(using = LocalTimeSerializer.class) // 직렬화
  @JsonDeserialize(using = LocalTimeDeserializer.class) // 역직렬화
  @JsonFormat(pattern = "HH:mm:ss")
  private LocalTime setTime;

  @Schema(description = "루틴 작성일", example = "2025-02-14")
  private String description;

  @Schema(description = "루틴 작성일", example = "2025-02-14")
  private int activityImp;
}
