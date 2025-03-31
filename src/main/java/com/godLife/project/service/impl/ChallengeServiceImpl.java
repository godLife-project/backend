package com.godLife.project.service.impl;

import com.godLife.project.dto.contents.ChallengeDTO;
import com.godLife.project.dto.infos.VerifyDTO;
import com.godLife.project.dto.request.ChallengeJoinRequest;
import com.godLife.project.enums.ChallengeState;
import com.godLife.project.mapper.ChallJoinMapper;
import com.godLife.project.mapper.ChallengeMapper;
import com.godLife.project.service.interfaces.ChallengeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ChallengeServiceImpl implements ChallengeService {
    private final ChallengeMapper challengeMapper;
    private final ChallJoinMapper challJoinMapper;

    public ChallengeServiceImpl(ChallengeMapper challengeMapper, ChallJoinMapper challengeJoinMapper) {
        this.challengeMapper = challengeMapper;
        this.challJoinMapper = challengeJoinMapper;
    }

    // 최신 챌린지 조회 (페이징 적용)
    @Override
    public List<ChallengeDTO> getLatestChallenges(int page, int size) {
        int offset = (page - 1) * size;
        return challengeMapper.getLatestChallenges(offset, size);
    }

    // 최신 챌린지 총 개수 조회
    @Override
    public int getTotalLatestChallenges() {
        return challengeMapper.countLatestChallenges();
    }

    // 카테고리별 챌린지 조회 (페이징 적용)
    @Override
    public List<ChallengeDTO> getChallengesByCategoryId(int categoryIdx, int page, int size) {
        int offset = (page - 1) * size;
        return challengeMapper.getChallengesByCategoryId(categoryIdx, offset, size);
    }

    // 카테고리별 챌린지 총 개수 조회
    @Override
    public int getTotalChallengesByCategory(int categoryIdx) {
        return challengeMapper.countChallengesByCategoryId(categoryIdx);
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
        ChallengeDTO challengeDTO = challengeMapper.challengeDetail(challIdx);
        // 시작시간이 설정되지 않은 경우에만 업데이트
        if (challengeDTO.getChallStartTime() == null) {
            LocalDateTime startTime = LocalDateTime.now(); // 첫 참가자 시점으로 시작시간 업데이트
            LocalDateTime endTime = startTime.plusDays(duration); // 시작시간 + 유지시간으로 종료시간 업데이트
            ChallengeState newState = ChallengeState.IN_PROGRESS; // 챌린지 상태 업데이트

            Map<String, Object> params = new HashMap<>();
            params.put("challIdx", challIdx);
            params.put("challStartTime", startTime);
            params.put("challEndTime", endTime);
            params.put("challState", newState.name());

            challengeMapper.updateChallengeStartTime(params);
        }
    }


    // 클라이언트에서 받는 값을 일수로 계산
    private LocalDateTime calculateEndTime(LocalDateTime challStartTime, int duration) {
        return challStartTime.plusDays(duration);
    }




    // 챌린지 상세 조회
    public ChallengeDTO getChallengeDetail(Long challIdx) {
        // 챌린지 기본 정보 조회
        ChallengeDTO challenge = challengeMapper.challengeDetail(challIdx);
        if (challenge == null) {
            throw new IllegalArgumentException("해당 챌린지를 찾을 수 없습니다: challIdx=" + challIdx);
        }

        // 현재 참여자 수 반영
        int participantCount = challengeMapper.countParticipants(challIdx);
        challenge.setCurrentParticipants(participantCount);

        // 인증을 통해 감소된 클리어 시간 반영
        int elapsedClearTime = challengeMapper.getElapsedClearTime(challIdx); // 인증을 통해 사용된 시간 조회
        int updatedClearTime = Math.max(0, challenge.getTotalClearTime() - elapsedClearTime); // 음수 방지
        challenge.setTotalClearTime(updatedClearTime); // 인증을 통한 총 클리어시간 감소 조회

        // 종료 시간이 되면 상태를 "완료됨"으로 변경
        if (LocalDateTime.now().isAfter(challenge.getChallEndTime())
                && !challenge.getChallState().equals(ChallengeState.COMPLETED.getState())) {
            challenge.setChallState(ChallengeState.COMPLETED.getState());
        }

        // DB 상태 업데이트 (최소화된 한 번의 업데이트)
        Map<String, Object> params = new HashMap<>();
        params.put("challIdx", challIdx);
        params.put("challStartTime", challenge.getChallStartTime());
        params.put("challEndTime", challenge.getChallEndTime());
        params.put("challState", challenge.getChallState());
        params.put("totalClearTime", challenge.getTotalClearTime());

        challengeMapper.updateChallengeStartTime(params);

        return challenge;
    }


    @Override
    public ChallengeDTO joinChallenge(Long challIdx, int userIdx, ChallengeJoinRequest joinRequest) {
        // 챌린지 기본 정보 조회
        ChallengeDTO challenge = challengeMapper.challengeDetail(challIdx);



        // 챌린지 상태가 "PUBLISHED" 또는 "IN_PROGRESS"일 때만 참여 가능
        if (!"PUBLISHED".equals(challenge.getChallState()) && !"진행중".equals(challenge.getChallState())) {
            throw new IllegalStateException("참여할 수 없는 챌린지입니다.");
        }

        // 챌린지 시작 시간이 null이면 (userJoin==1 이면) 첫 번째 유저의 참여 시점으로 시작 시간 설정
        if (challenge.getChallStartTime() == null) {
            LocalDateTime now = LocalDateTime.now();
            challenge.setChallStartTime(now);  // 첫 번째 참여자 시작 시간 설정
            challenge.setChallState(ChallengeState.IN_PROGRESS.getState());  // 상태를 '진행중'으로 변경
        }

        // 참가 인원수 조회 ( 현재인원 체크 )
        int currentParticipants = challengeMapper.countParticipants(challIdx);
        int maxParticipants = challenge.getMaxParticipants();

        if (currentParticipants >= maxParticipants){
            throw new IllegalArgumentException("참가 인원 수가 초과되어 더 이상 참가할 수 없습니다.");
        }

        // 유저 최초 참여 시점에서 시작시간, 종료시간 설정 (클라이언트에서 받은 시간 사용)
        LocalDateTime now = LocalDateTime.now();
        if (challenge.getChallStartTime() == null) {  // 첫 번째 참여자일 때 시작 시간 설정
            challenge.setChallStartTime(now);
            challenge.setChallState(ChallengeState.IN_PROGRESS.getState());  // 상태를 '진행중'으로 변경
        }

        // 종료시간 계산 (클라이언트에서 받은 duration을 사용하여 종료 시간 계산)
        LocalDateTime challEndTime = joinRequest.getChallEndTime();
        if (challEndTime == null) {  // 종료 시간이 클라이언트에서 주어지지 않으면 계산
            challEndTime = calculateEndTime(now, joinRequest.getDuration());
        }

        challenge.setChallEndTime(challEndTime);

        // DB에 시작시간과 종료시간, 상태 업데이트
        Map<String, Object> params = new HashMap<>();
        params.put("challIdx", challIdx);
        params.put("challStartTime", challenge.getChallStartTime());
        params.put("challEndTime", challenge.getChallEndTime());
        params.put("challState", challenge.getChallState());

        challengeMapper.updateChallengeStartTime(params);

        // 사용자 참여 기록 저장 (챌린지/유저 인덱스, 활동명)
        challengeMapper.addUserToChallenge(
                challIdx,
                userIdx,
                joinRequest.getChallStartTime(),
                challEndTime,
                joinRequest.getActivity()
        );

        // 챌린지 정보 반환
        return challenge;
    }

    @Override
    public void verifyChallenge(VerifyDTO verifyDTO) {
        LocalDateTime startTime = verifyDTO.getVerifyDate();
        LocalDateTime endTime = LocalDateTime.now();

        // 경과시간 계산 (분 단위)
        int elapsedMinutes = (int) java.time.Duration.between(startTime, endTime).toMinutes();
        verifyDTO.setElapsedTime(elapsedMinutes);

        // 인증 기록 저장
        challengeMapper.insertVerifyRecord(verifyDTO);

        // 최신 챌린지 정보 조회
        ChallengeDTO challenge = challengeMapper.challengeDetail(verifyDTO.getChallIdx());

        // 남은 클리어 시간 업데이트 (자동으로 챌린지 종료 처리 포함)
        challengeMapper.updateClearTime(verifyDTO.getChallIdx(), elapsedMinutes);
    }


    // 챌린지 존재 여부 확인 (서비스 구현 수정)
    @Override
    public boolean existsById(Long challIdx) {
        return challengeMapper.existsById(challIdx) > 0;  // 0보다 크면 존재하는 챌린지
    }

    // 챌린지 수정
    @Override
    public int modifyChallenge(ChallengeDTO challengeDTO) {
        int updatedCount = challengeMapper.modifyChallenge(challengeDTO);
        // 1건만 수정이 되는지 확인
        if (updatedCount != 1) {
            throw new IllegalArgumentException("챌린지 수정 실패");
        }
        return updatedCount;
    }

    // 챌린지 삭제
    @Override
    public int deleteChallenge(ChallengeDTO challengeDTO) {
        // 삭제 수행
        int result = challengeMapper.deleteChallenge(challengeDTO);
        if (result == 0) {
            return 500;
        }
        return 200; // 삭제 성공

    }
}

