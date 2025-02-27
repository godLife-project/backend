package com.godLife.project.service;

import com.godLife.project.dto.contents.ChallengeDTO;
import com.godLife.project.dto.infos.VerifyDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface ChallengeService {
    // 최신 챌린지 리스트
    List<ChallengeDTO> getLatestChallenges();

    // 챌린지 생성
    void createChallenge(ChallengeDTO challengeDTO) throws Exception;

    void joinChallenge(Long challIdx, int userIdx, LocalDateTime challEndTime);

    void verifyChallenge(VerifyDTO verifyDTO, ChallengeDTO challengeDTO);
}
