package com.godLife.project.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChallRequestDTO {
  private Long challIdx;
  private String challTitle;
  private String challDescription;
  private Integer challCategoryIdx;
  private Integer maxParticipants;
  private LocalDateTime challEndTime;
  private Integer currentParticipants;
}
