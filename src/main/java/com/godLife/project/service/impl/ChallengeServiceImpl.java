package com.godLife.project.service.impl;

import com.godLife.project.dto.contents.ChallengeDTO;
import com.godLife.project.dto.infos.VerifyDTO;
import com.godLife.project.enums.ChallengeState;
import com.godLife.project.mapper.ChallengeJoinMapper;
import com.godLife.project.mapper.ChallengeMapper;
import com.godLife.project.service.ChallengeService;
import org.springframework.stereotype.Service;

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
    // 카테고리별 챌린지 조회
    public List<ChallengeDTO> getChallengesByCategoryId(int categoryIdx) {
        return challengeMapper.getChallengesByCategoryId(categoryIdx);
    }

    public void createChallenge(ChallengeDTO challengeDTO) throws Exception {
        // 챌린지 기본값 설정 (권한 체크 없음)
        challengeDTO.setChallState(ChallengeState.PUBLISHED.name());

        // 챌린지 생성
        challengeMapper.createChallenge(challengeDTO);
    }

    @Override
    public void joinChallenge(Long challIdx, int userIdx, LocalDateTime challEndTime) {
        ChallengeDTO challenge = challengeMapper.getChallenge(challIdx);

        if ("PUBLISHED".equals(challenge.getChallState())) {
            // 최초 참여
            LocalDateTime now = LocalDateTime.now();

            // 관리자가 종료일을 지정하지 않으면 예외 발생
            if (challEndTime == null) {
                throw new IllegalArgumentException("챌린지 종료 시간을 지정해야 합니다.");
            }

            challenge.setChallStartTime(now);
            challenge.setChallEndTime(challEndTime); // 관리자가 지정한 종료 시간
            challenge.setChallState("IN_PROGRESS");

            challengeMapper.startChallenge(challenge);
        }
    }
    @Override
    public void verifyChallenge(VerifyDTO verifyDTO, ChallengeDTO challengeDTO) {
        LocalDateTime startTime = verifyDTO.getVerifyDate();
        LocalDateTime endTime = LocalDateTime.now();

        // 경과시간 계산 (분 단위)
        int elapsedMinutes = (int) java.time.Duration.between(startTime, endTime).toMinutes();
        verifyDTO.setElapsedTime(elapsedMinutes);

        // 인증 기록 저장
        challengeMapper.insertVerifyRecord(verifyDTO);

        // 남은 클리어 시간 계산 및 차감
        int remainingTime = challengeDTO.getTotalClearTime() - elapsedMinutes;
        if (remainingTime < 0) {
            remainingTime = 0;  // 클리어 시간이 음수가 되지 않도록 처리
        }

        // 남은 클리어 시간 업데이트
        challengeMapper.updateClearTime(verifyDTO.getChallIdx(), elapsedMinutes);

        // 클리어 시간 0이면 챌린지 종료 처리
        ChallengeDTO challenge = challengeMapper.getChallenge((Long) verifyDTO.getChallIdx());
        if (challenge.getTotalClearTime() <= 0) {
            challengeMapper.finishChallenge(verifyDTO.getChallIdx());
        }
    }
    // 챌린지 수정
    public void modifyChallenge(ChallengeDTO challengeDTO) {
        int updatedCount = challengeMapper.modifyChallenge(challengeDTO);
        // 1건만 수정이 되는지 확인
        if (updatedCount != 1) {
            throw new IllegalArgumentException("챌린지 수정 실패");
        }
    }

    // 챌린지 삭제
    public void deleteChallenge(Long challIdx){
        int deleteCount = challengeMapper.deleteChallenge(challIdx);
        if (deleteCount != 1) {
            throw new IllegalArgumentException("챌린지 삭제 실패");
        }
    }

}

