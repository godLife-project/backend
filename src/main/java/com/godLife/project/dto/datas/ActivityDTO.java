package com.godLife.project.dto.datas;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalTime;

@Data
public class ActivityDTO {
  @Schema(description = "활동 인덱스", example = "1")
  private int activityIdx;

  @Schema(description = "루틴 인덱스", example = "1")
  private int planIdx;

  @Schema(description = "활동명", example = "조깅 2시간")
  @NotBlank(message = "{writeActivity.activityName.notBlank}")
  private String activityName;

  @Schema(description = "시작 시간", example = "08:00")
  @JsonSerialize(using = LocalTimeSerializer.class) // 직렬화
  @JsonDeserialize(using = LocalTimeDeserializer.class) // 역직렬화
  @JsonFormat(pattern = "HH:mm")
  private LocalTime setTime;

  @Schema(description = "한줄 메모", example = "이렇게 할 생각")
  private String description;

  @Schema(description = "활동 중요도", example = "4")
  private int activityImp;

  @Schema(description = "활동 인증 여부", example = "false")
  private boolean verified;
}
