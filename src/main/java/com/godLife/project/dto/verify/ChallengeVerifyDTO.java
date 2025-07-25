package com.godLife.project.dto.verify;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChallengeVerifyDTO {

  @Schema(description = "챌린지 인덱스", example = "1")
  private Long challIdx;

  @Schema(description = "유저 인덱스", example = "2")
  private Long userIdx;

  @Schema(description = "활동 시작 시간", example = "2025-04-07T08:00:00")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime startTime;

  @Schema(description = "활동 종료 시간", example = "2025-04-07T09:30:00")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime endTime;

  @Schema(description = "활동명", example = "조깅")
  private String activity;
}
