package com.godLife.project.dto.scheduler;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyUnder90DTO {
  private int planIdx;
  private int certExp;
  private int lastExp;
  private double verifyRate;

  private int minusExp;
  private int expLimit = 100;
}
