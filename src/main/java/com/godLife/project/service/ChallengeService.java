package com.godLife.project.service;

import com.godLife.project.dto.contents.ChallengeDTO;

import java.util.List;

public interface ChallengeService {
    // 최신 챌린지 리스트
    List<ChallengeDTO> getLatestChallenges();
}
