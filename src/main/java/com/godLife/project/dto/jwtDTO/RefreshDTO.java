package com.godLife.project.dto.jwtDTO;

import lombok.Data;

import java.util.Date;

@Data
public class RefreshDTO {

  private int tokenIdx;

  private String username;
  private String refresh;
  private Date expiration;
}
