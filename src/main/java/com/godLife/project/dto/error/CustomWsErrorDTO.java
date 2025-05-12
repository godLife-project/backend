package com.godLife.project.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomWsErrorDTO {
  private String message;
  private int code;
}
