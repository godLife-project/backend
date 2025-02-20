package com.godLife.project.service;

import com.godLife.project.dto.contents.ChallengeDTO;
import com.godLife.project.mapper.ChallengeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChallengeServicelmpl implements ChallengeService {
    private final ChallengeMapper challengeMapper;
    public ChallengeServicelmpl(ChallengeMapper challengeMapper) {this.challengeMapper = challengeMapper;}

    // 최신 챌린지 가져오기
    public List<ChallengeDTO> getLatestChallenges() {
        return challengeMapper.getLatestChallenges();
    }
}
