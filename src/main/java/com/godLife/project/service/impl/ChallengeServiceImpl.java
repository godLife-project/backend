package com.godLife.project.service.impl;

import com.godLife.project.dto.contents.ChallengeDTO;
import com.godLife.project.dto.infos.ChallengeJoinDTO;
import com.godLife.project.dto.infos.VerifyDTO;
import com.godLife.project.enums.ChallengeState;
import com.godLife.project.mapper.ChallJoinMapper;
import com.godLife.project.mapper.ChallengeMapper;
import com.godLife.project.service.interfaces.ChallengeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ChallengeServiceImpl implements ChallengeService {
    private ChallengeMapper challengeMapper;
    private ChallJoinMapper challJoinMapper;

    public ChallengeServiceImpl(ChallengeMapper challengeMapper, ChallJoinMapper challengeJoinMapper) {
        this.challengeMapper = challengeMapper;
        this.challJoinMapper = challengeJoinMapper;
    }

    // 최신 챌린지 가져오기
    public List<ChallengeDTO> getLatestChallenges() {
        // 종료된 챌린지를 제외하고 최신 챌린지 리스트를 조회
        List<ChallengeDTO> challengeList = challengeMapper.getLatestChallenges();

        // 각 챌린지의 참여 인원 수 조회 및 설정
        for (ChallengeDTO challenge : challengeList) {
            int participantCount = challengeMapper.countParticipants(challenge.getChallIdx());
            challenge.setCurrentParticipants(participantCount);
        }

        return challengeList;
    }


    // 카테고리별 챌린지 조회
    public List<ChallengeDTO> getChallengesByCategoryId(int categoryIdx) {
        List<ChallengeDTO> challengeList = challengeMapper.getChallengesByCategoryId(categoryIdx);

        // 각 챌린지의 참여 인원 수 조회 및 설정
        for (ChallengeDTO challenge : challengeList) {
            int participantCount = challengeMapper.countParticipants(challenge.getChallIdx());
            challenge.setCurrentParticipants(participantCount);
        }

        return challengeList;
    }

    // 챌린지 작성
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createChallenge(ChallengeDTO challengeDTO) {
        try {
            // 챌린지 기본값 설정 (권한 체크 없음)
            challengeDTO.setChallState(ChallengeState.PUBLISHED.name());

            // 관리자 개입형 (시작, 종료시간 설정)
            if (challengeDTO.getUserJoin() == 0) {
                if (challengeDTO.getChallStartTime() == null || challengeDTO.getDuration() == null) {
                    throw new IllegalArgumentException("관리자 개입형 챌린지는 시작 시간과 기간(Duration)이 필요합니다.");
                }

                // 종료 시간 설정
                LocalDateTime startTime = challengeDTO.getChallStartTime();
                Integer duration = challengeDTO.getDuration();
                LocalDateTime endTime = startTime.plusDays(duration);

                challengeDTO.setChallEndTime(endTime);


            } // 유저 참여형 (시작, 종료시간을 null로 유지)
            else if (challengeDTO.getUserJoin() == 1) {
                challengeDTO.setChallStartTime(null); // 참가자가 생길 때 설정
                challengeDTO.setChallEndTime(null);
            }

            // 챌린지 생성
            challengeMapper.createChallenge(challengeDTO);
            return 201;
        } catch (Exception e) {
            log.error("e: ", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 수동 롤백
            return 500;
        }
    }



    public void updateChallengeStartTime(Long challIdx, Integer duration) throws Exception {
        ChallengeDTO challengeDTO = challengeMapper.ChallengeDetail(challIdx);
        // 시작시간이 설정되지 않은 경우에만 업데이트
        if (challengeDTO.getChallStartTime() != null) {
            LocalDateTime startTime = LocalDateTime.now(); // 첫 참가자 시점으로 시작시간 업데이트
            LocalDateTime endTime = startTime.plusDays(duration); // 시작시간 + 유지시간으로 종료시간 업데이트
            ChallengeState newState = ChallengeState.IN_PROGRESS; // 챌린지 상태 업데이트
            challengeMapper.updateChallengeStartTime(challIdx, startTime, endTime, newState.name());
        }
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
        challengeMapper.updateChallengeStartTime(
                challIdx,
                challenge.getChallStartTime(),
                challenge.getChallEndTime(),
                challenge.getChallState()
        );

        return challenge;
    }

    public void enterChallenge(ChallengeJoinDTO challengeJoinDTO) {

    }


    @Override
    public ChallengeDTO joinChallenge(Long challIdx, int userIdx, Integer duration, LocalDateTime startTime, LocalDateTime endTime, String activity) {
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
        challengeMapper.updateChallengeStartTime(
                challIdx,
                challenge.getChallStartTime(),
                challenge.getChallEndTime(),
                challenge.getChallState()
        );

        // 사용자 참여 기록 저장 (챌린지/유저 인덱스, 활동명)
        challengeMapper.addUserToChallenge(challIdx, userIdx, startTime, endTime, activity);
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
    // 챌린지 존재 여부 확인
    @Override
    public boolean existsById(Long challIdx) {
        return challengeMapper.existsById(challIdx);  // 0보다 크면 존재하는 챌린지
    }

    // 챌린지 수정
    public int modifyChallenge(ChallengeDTO challengeDTO) {
        int updatedCount = challengeMapper.modifyChallenge(challengeDTO);
        // 1건만 수정이 되는지 확인
        if (updatedCount != 1) {
            throw new IllegalArgumentException("챌린지 수정 실패");
        }
        return updatedCount;
    }

    // 챌린지 삭제
    public int deleteChallenge(ChallengeDTO challengeDTO) {
        // 삭제 수행
        int result = challengeMapper.deleteChallenge(challengeDTO);
        if (result == 0) {
            return 500;
        }
        return 200; // 삭제 성공

    }
}

