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

    // 챌린지 작성
    public void createChallenge(ChallengeDTO challengeDTO) throws Exception {
        // 챌린지 기본값 설정 (권한 체크 없음)
        challengeDTO.setChallState(ChallengeState.PUBLISHED.name());

        // 관리자 개입형 / 유저 친화적 구분
        if (challengeDTO.getUserJoin() == 0) {  // 관리자 개입형
            if (challengeDTO.getChallStartTime() != null && challengeDTO.getDuration() != null) {
                LocalDateTime startTime = challengeDTO.getChallStartTime(); // 시작시간
                Integer duration = challengeDTO.getDuration(); // 유지시간 (일 단위)
                LocalDateTime endTime = startTime.plusDays(duration); // 종료시간 계산
                challengeDTO.setChallEndTime(endTime); // 종료시간 설정
            }
        } else if (challengeDTO.getUserJoin() == 1) {  // 유저 친화적
            // 유저 참여형에서는 시작시간을 지정할 수 없도록 체크
            if (challengeDTO.getChallStartTime() != null) {
                throw new IllegalArgumentException("유저 참여형 챌린지는 시작 시간을 지정할 수 없습니다.");
            }

            // 시작시간이 null일 경우 최초 참여자 시점으로 시작시간 설정
            if (challengeDTO.getChallStartTime() == null) {
                LocalDateTime startTime = LocalDateTime.now(); // 현재 시점으로 시작시간 설정
                challengeDTO.setChallStartTime(startTime); // 시작시간 설정
            }

            // 종료시간은 유저가 참여하면서 결정되므로 별도로 설정하지 않음
            challengeDTO.setChallEndTime(null);  // 종료시간은 null로 설정 (나중에 결정)
        }

        // 챌린지 생성
        challengeMapper.createChallenge(challengeDTO);
    }


    // 클라이언트에서 받는 값을 일수로 계산
    private LocalDateTime calculateEndTime(LocalDateTime challStartTime, int duration) {
        return challStartTime.plusDays(duration);
    }

    // 챌린지 상세 조회 및 상태 변경
    public ChallengeDTO getChallnegeDetail(Long challIdx, int userJoin, Integer duration) {
        // 챌린지 기본 정보 조회
        ChallengeDTO challenge = challengeMapper.ChallengeDetail(challIdx);
        //현재 참여자수 조회
        int participantCount = challengeMapper.countParticipants(challIdx);
        challenge.setCurrentParticipants(participantCount);
        // 참여 형태별 처리
        if (userJoin == 0){ // 관리자 개입형
            if (challenge.getChallStartTime() == null) {
                throw new IllegalArgumentException("관리자 개입형 챌린지는 시작시간이 필수입니다.");
            }
            // 종료시간 계산
            challenge.setChallEndTime(calculateEndTime(challenge.getChallStartTime(), challenge.getTotalClearTime()));
            // 시작시간 설정 후 상태 '진행중' 업데이트
            challenge.setChallState(ChallengeState.IN_PROGRESS.getState());


        } else if (userJoin == 1){ // 유저 참여형
            if ("PUBLISHED".equals(challenge.getChallState())) {
                // 유저 최초 참여 시점에서 시작시간, 상태변경
                challenge.setChallStartTime(LocalDateTime.now());
                challenge.setChallState(ChallengeState.IN_PROGRESS.getState());
                // 종료시간 계산
                challenge.setChallEndTime(calculateEndTime(challenge.getChallStartTime(), duration));
            }

        }
            // 종료 시간이 되면 상태를 "완료됨"으로 변경
        if (LocalDateTime.now().isAfter(challenge.getChallEndTime())
                && !challenge.getChallState().equals(ChallengeState.COMPLETED.getState())) {
            challenge.setChallState(ChallengeState.COMPLETED.getState());
        }

        // DB 상태 업데이트 (최소화된 한 번의 업데이트)
        challengeMapper.updateChallengeStartTimeAndState(
                challIdx,
                challenge.getChallStartTime(),
                challenge.getChallEndTime(),
                challenge.getChallState()
        );

        return challenge;
    }



    @Override
    public ChallengeDTO joinChallenge(Long challIdx, int userIdx, Integer duration) {
        // 챌린지 기본 정보 조회
        ChallengeDTO challenge = challengeMapper.ChallengeDetail(challIdx);

        // 챌린지 상태가 "게시중", "진행중" 일 때만 참여 가능
        if (!"PUBLISHED".equals(challenge.getChallState()) && !"IN_PROGRESS".equals(challenge.getChallState())) {
            throw new IllegalStateException("참여할 수 없는 챌린지입니다.");
        }

        // 유저 최초 참여 시점에서 시작시간, 종료시간 설정
        LocalDateTime now = LocalDateTime.now();
        challenge.setChallStartTime(now);
        challenge.setChallState(ChallengeState.IN_PROGRESS.getState());

        // 종료시간 계산
        challenge.setChallEndTime(calculateEndTime(now, duration));

        // DB에 시작시간과 종료시간, 상태 업데이트
        challengeMapper.updateChallengeStartTimeAndState(
                challIdx,
                challenge.getChallStartTime(),
                challenge.getChallEndTime(),
                challenge.getChallState()
        );

        // 사용자 참여 정보 기록 (CHALL_JOIN 테이블에)
        challengeMapper.addUserToChallenge(challIdx, userIdx);
        return challenge;
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
        ChallengeDTO challenge = challengeMapper.ChallengeDetail((Long) verifyDTO.getChallIdx());
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

