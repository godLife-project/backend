package com.godLife.project.service;

import com.godLife.project.dto.contents.ChallengeDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChallengeService {
    // 최신 챌린지 리스트
    List<ChallengeDTO> getLatestChallenges();

    // 챌린지 생성
    void createChallenge(ChallengeDTO challengeDTO) throws Exception;

    @Transactional
    void joinChallenge(Long userId, int challIdx) throws Exception;
}
