package com.godLife.project.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class PlanRequestDTO {
  private int planIdx;
  private int userIdx;
  @Min(0)
  @Max(1)
  private int isActive;
}
