package com.godLife.project.dto.jwt;

import lombok.Data;

@Data
public class RefreshDTO {

  private int tokenIdx;

  private String username;
  private String refresh;
  private String expiration;
}
