package com.godLife.project.dto.response;

import lombok.Data;

@Data
public class LoginResponseDTO {
  private int userIdx;
  private String userName;
  private String userNick;
  private String nickTag;
  private int jobIdx;
  private int targetIdx;
  private int combo;
  private double userExp;
  private int userLv;
  // private int userGender;    // 필요시 사용
  // private int authorityIdx; // 필요시 사용
  private boolean roleStatus;
}
