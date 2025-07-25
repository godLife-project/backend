package com.godLife.project.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChallengeJoinRequest {
  private int userIdx; // 유저 ID
  private int duration; // 지속 시간
  private LocalDateTime StartTime; // 챌린지 시작 시간
  private LocalDateTime EndTime; // 챌린지 종료
  private String activity;
  private int activityTime;


  // 생성자, Getter, Setter
}
