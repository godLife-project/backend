package com.godLife.project.service.impl.scheduleImpl;

import com.godLife.project.mapper.ChallengeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ChallengeStatusScheduler {
  private final ChallengeMapper challengeMapper;

  // 매 5분마다 실행
  @Scheduled(fixedRate = 300_000) // 5분
  public void updateChallengeStatusToEnded() {
    challengeMapper.updateChallengesToEndStatus(LocalDateTime.now());
  }
}
