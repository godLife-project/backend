package com.godLife.project.service.impl;

import com.godLife.project.dto.contents.ChallengeDTO;
import com.godLife.project.enums.ChallengeState;
import com.godLife.project.mapper.ChallengeJoinMapper;
import com.godLife.project.mapper.ChallengeMapper;
import com.godLife.project.service.ChallengeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChallengeServiceImpl implements ChallengeService {
    private ChallengeMapper challengeMapper;
    private ChallengeJoinMapper challengeJoinMapper;

    public ChallengeServiceImpl(ChallengeMapper challengeMapper, ChallengeJoinMapper challengeJoinMapper) {
        this.challengeMapper = challengeMapper;
        this.challengeJoinMapper = challengeJoinMapper;
    }
    // 최신 챌린지 가져오기
    public List<ChallengeDTO> getLatestChallenges() {
        // 종료된 챌린지를 제외하고 최신 챌린지 리스트를 조회
        return challengeMapper.getLatestChallenges();
    }

    public void createChallenge(ChallengeDTO challengeDTO) throws Exception {
        // 챌린지 기본값 설정 (권한 체크 없음)
            challengeDTO.setChallState(ChallengeState.PUBLISHED.name());

            // 챌린지 생성
            challengeMapper.createChallenge(challengeDTO);
        }


    // 챌린지 참여 로직
    @Transactional
    @Override
    public void joinChallenge(Long userId, int challIdx) throws Exception {
        // 이미 참여했는지 확인
        if (challengeJoinMapper.isUserJoined(userId, challIdx)) {
            throw new IllegalStateException("이미 참여한 챌린지입니다.");
        }

        // 챌린지 정보 가져오기
        ChallengeDTO challenge = challengeMapper.getChallengeById(challIdx);
        if (challenge == null) {
            throw new IllegalArgumentException("존재하지 않는 챌린지입니다.");
        }

        // 최대 참여 인원 확인
        int currentParticipants = challengeJoinMapper.getCurrentParticipants(challIdx);
        if (currentParticipants >= challenge.getMaxParticipants()) {
            throw new IllegalStateException("최대 참여 인원을 초과했습니다.");
        }

        // 챌린지 참여 추가
        challengeJoinMapper.insertChallengeJoin(userId, challIdx);

        // 챌린지 시작 시간이 없으면 현재 시간으로 업데이트 및 종료 시간 설정
        if (challenge.getChallStartTime() == null) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime endTime = now.plusHours(challenge.getTotalClearTime());

            challengeMapper.updateChallengeStartTime(challIdx, now, endTime);
            challengeMapper.updateChallengeState(challIdx, ChallengeState.IN_PROGRESS.name());
        }
    }



}

