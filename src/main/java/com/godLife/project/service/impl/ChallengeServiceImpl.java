package com.godLife.project.service.impl;

import com.godLife.project.dto.contents.ChallengeDTO;
import com.godLife.project.dto.infos.ChallengeJoinDTO;
import com.godLife.project.dto.verify.ChallengeVerifyDTO;
import com.godLife.project.dto.verify.VerifyRecordDTO;
import com.godLife.project.enums.ChallengeState;
import com.godLife.project.mapper.ChallengeMapper;
import com.godLife.project.service.interfaces.ChallengeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {
    private final ChallengeMapper challengeMapper;

    // ----------------- 최신 챌린지 조회 (페이징 적용) -----------------
    @Override
    public List<ChallengeDTO> getLatestChallenges(int page, int size) {
        // 진행 중인 챌린지를 종료 상태로 자동 변경
        challengeMapper.updateChallengesToEndStatus(LocalDateTime.now());
        int offset = (page - 1) * size;
        return challengeMapper.getLatestChallenges(offset, size);
    }
    // 최신 챌린지 총 개수 조회
    @Override
    public int getTotalLatestChallenges() {
        return challengeMapper.countLatestChallenges();
    }


    // -----------------카테고리별 챌린지 조회 (페이징 적용) -----------------
    @Override
    public List<ChallengeDTO> getChallengesByCategoryId(int categoryIdx, int page, int size) {
        challengeMapper.updateChallengesToEndStatus(LocalDateTime.now());
        int offset = (page - 1) * size;
        return challengeMapper.getChallengesByCategoryId(categoryIdx, offset, size);
    }
    // 카테고리별 챌린지 총 개수 조회
    @Override
    public int getTotalChallengesByCategory(int categoryIdx) {
        return challengeMapper.countChallengesByCategoryId(categoryIdx);
    }


    // ----------------- 챌린지 상세 조회 -----------------
    public ChallengeDTO getChallengeDetail(Long challIdx) {
        // 챌린지 기본 정보 조회
        ChallengeDTO challenge = challengeMapper.challengeDetail(challIdx);
        if (challenge == null) {
            throw new IllegalArgumentException("해당 챌린지를 찾을 수 없습니다: challIdx=" + challIdx);
        }

        // 현재 참여자 수
        int participantCount = challengeMapper.countParticipants(challIdx);
        challenge.setCurrentParticipants(participantCount);


        // 인증을 통해 감소된 클리어 시간 반영
        int elapsedClearTime = challengeMapper.getElapsedClearTime(challIdx); // 인증을 통해 사용된 시간 조회
        int updatedClearTime = Math.max(0, challenge.getTotalClearTime() - elapsedClearTime); // 음수 방지
        challenge.setTotalClearTime(updatedClearTime); // 인증을 통한 총 클리어시간 감소 조회

        // 종료 시간이 되면 상태를 "종료" 로 변경
        if (challenge.getChallEndTime() != null
                && LocalDateTime.now().isAfter(challenge.getChallEndTime())
                && !challenge.getChallState().equals(ChallengeState.END.getCode())
                && !challenge.getChallState().equals(ChallengeState.COMPLETED.getCode())) {
            challenge.setChallState(ChallengeState.END.getCode());
        }

        // 참가자 상세 정보 조회 및 설정
        List<ChallengeJoinDTO> participants = challengeMapper.getChallengeParticipants(challIdx);
        challenge.setParticipants(participants);


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

    // 인증기록 조회
    public List<VerifyRecordDTO> getVerifyRecords(Long challIdx, Long userIdx) {
        if (challIdx == null || userIdx == null) {
            throw new IllegalArgumentException("챌린지 ID 또는 사용자 ID가 null입니다.");
        }
        return challengeMapper.getVerifyRecords(challIdx, userIdx);
    }


    // ----------------- 챌린지 참여 시 시작 시간 업데이트 -----------------
    public void updateChallengeStartTime(Long challIdx) throws Exception {
        ChallengeDTO challengeDTO = challengeMapper.challengeDetail(challIdx);

        if (challengeDTO.getUserJoin() == 1 && challengeDTO.getChallStartTime() == null) {
            LocalDateTime startTime = LocalDateTime.now();

            // ⬇ duration 컬럼에서 직접 가져옴
            Integer duration = challengeDTO.getDuration();
            if (duration == null || duration <= 0) {
                throw new IllegalArgumentException("챌린지의 duration 값이 유효하지 않습니다.");
            }

            LocalDateTime endTime = calculateEndTime(startTime, duration);

            challengeDTO.setChallStartTime(startTime);
            challengeDTO.setChallEndTime(endTime);
            challengeDTO.setChallState(ChallengeState.IN_PROGRESS.getCode());

            Map<String, Object> params = new HashMap<>();
            params.put("challIdx", challIdx);
            params.put("challStartTime", startTime);
            params.put("challEndTime", endTime);
            params.put("challState", challengeDTO.getChallState());

            challengeMapper.updateChallengeStartTime(params);
        } else if (challengeDTO.getUserJoin() == 1 && challengeDTO.getChallStartTime() != null) {
            throw new IllegalStateException("이미 진행 중인 챌린지입니다.");
        } else if (challengeDTO.getUserJoin() == 0) {
            throw new IllegalArgumentException("관리자 개입형 챌린지는 이 메서드로 시작할 수 없습니다.");
        }
    }

    // 종료 시간 계산 로직
    private LocalDateTime calculateEndTime(LocalDateTime challStartTime, int duration) {
        return challStartTime.plusDays(duration);
    }



    // ----------------- 챌린지 참여 -----------------
    @Override
    public ChallengeDTO joinChallenge(Long challIdx, int userIdx, String activity, int activityTime) {
        // 챌린지 기본 정보 조회
        ChallengeDTO challenge = challengeMapper.challengeDetail(challIdx);

        // 챌린지 상태가 참여 가능한 상태인지 확인
        if (!"PUBLISHED".equals(challenge.getChallState()) && !"IN_PROGRESS".equals(challenge.getChallState())) {
            throw new IllegalStateException("참여할 수 없는 챌린지입니다.");
        }

        // 중복 참여 여부 확인
        ChallengeJoinDTO joinInfo = challengeMapper.getJoinInfo(challIdx, (long) userIdx);
        if (joinInfo != null) {
            throw new IllegalArgumentException("이미 참여한 챌린지입니다. challIdx : " + challIdx);
        }

        // 참가 인원 수 확인
        int currentParticipants = challengeMapper.countParticipants(challIdx);
        int maxParticipants = challenge.getMaxParticipants();
        if (currentParticipants >= maxParticipants) {
            throw new IllegalArgumentException("참가 인원 수가 초과되어 더 이상 참가할 수 없습니다. challIdx : " + challIdx);
        }

        LocalDateTime now = LocalDateTime.now();


        // 유저 참여형 챌린지이고 아직 시작되지 않았다면 → 시작 처리
        if (challenge.getUserJoin() == 1 && challenge.getChallStartTime() == null) {
            challenge.setChallStartTime(now);
            challenge.setChallState(ChallengeState.IN_PROGRESS.getCode());

            // duration은 서버 컬럼 기준으로 계산
            LocalDateTime challEndTime = calculateEndTime(now, challenge.getDuration());
            challenge.setChallEndTime(challEndTime);

            // DB 업데이트
            Map<String, Object> params = new HashMap<>();
            params.put("challIdx", challIdx);
            params.put("challStartTime", now);
            params.put("challEndTime", challEndTime);
            params.put("challState", challenge.getChallState());
            challengeMapper.updateChallengeStartTime(params);
        }

        // 참여 유저의 종료시간 계산 (관리자 챌린지라면 이미 존재하는 종료시간 사용)
        LocalDateTime userEndTime = challenge.getChallEndTime();

        // 사용자 참여 기록 저장
        challengeMapper.addUserToChallenge(
                challIdx,
                userIdx,
                activity,
                activityTime
        );

        return challenge;
    }

    // ----------------- 챌린지 인증 -----------------
    public void verifyChallenge(ChallengeVerifyDTO challengeVerifyDTO) {
        // 1. 챌린지 존재 여부 확인
        if (!existsById(challengeVerifyDTO.getChallIdx())) {
            throw new IllegalArgumentException("존재하지 않는 챌린지입니다.");
        }

        // 2. 챌린지 참여 여부 확인
        ChallengeJoinDTO joinInfo = challengeMapper.getJoinInfo(
                challengeVerifyDTO.getChallIdx(),
                challengeVerifyDTO.getUserIdx()
        );

        if (joinInfo == null) {
            throw new IllegalArgumentException("챌린지에 참여하지 않았습니다.");
        }

        //  3. 하루에 한 번 인증 여부 확인
        if (hasAlreadyVerifiedToday(challengeVerifyDTO.getUserIdx(), challengeVerifyDTO.getChallIdx())) {
            throw new IllegalStateException("오늘은 이미 인증을 완료했습니다. 내일 다시 시도해주세요.");
        }

        // 4. 유효성 검사
        if (challengeVerifyDTO.getStartTime() == null || challengeVerifyDTO.getEndTime() == null) {
            throw new IllegalArgumentException("시작 시간과 종료 시간을 모두 입력해야 합니다.");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = challengeVerifyDTO.getStartTime();
        LocalDateTime endTime = challengeVerifyDTO.getEndTime();

        // start_time, end_time이 현재보다 미래이면 에러
        if (startTime.isAfter(now) || endTime.isAfter(now)) {
            throw new IllegalArgumentException("시작/종료 시간은 현재 시간보다 이전이어야 합니다.");
        }
        // end_time은 start_time 보다 이후여야 함
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("종료 시간은 시작 시간 이후여야 합니다.");
        }

        if (challengeVerifyDTO.getActivity() == null || challengeVerifyDTO.getActivity().trim().isEmpty()) {
            throw new IllegalArgumentException("활동명을 입력해야 합니다.");
        }

        // 시작 시간이 하루(24시간) 이상 지난 경우 인증 불가
        if (startTime.isBefore(now.minusDays(1))) {
            throw new IllegalStateException("하루가 지난 활동은 인증할 수 없습니다.");
        }

        // 6. 활동 시간 계산 (분 단위)
        long elapsedTime = Duration.between(
                challengeVerifyDTO.getStartTime(),
                challengeVerifyDTO.getEndTime()
        ).getSeconds() / 60;

        // 7. 인증 기록 저장
        challengeMapper.insertVerify(
                challengeVerifyDTO.getChallIdx(),
                challengeVerifyDTO.getUserIdx(),
                challengeVerifyDTO.getStartTime(),
                challengeVerifyDTO.getEndTime(),
                elapsedTime
        );

        // 8. 활동 정보 갱신
        challengeMapper.updateChallJoin(
                challengeVerifyDTO.getChallIdx(),
                Math.toIntExact(challengeVerifyDTO.getUserIdx()),
                challengeVerifyDTO.getStartTime(),
                challengeVerifyDTO.getEndTime(),
                challengeVerifyDTO.getActivity()
        );

        // 9. 챌린지 남은 시간 차감
        challengeMapper.updateClearTime(
                challengeVerifyDTO.getChallIdx(),
                (int) elapsedTime
        );
        // 10. 총 클리어 시간이 0 이하일 경우 챌린지를 '종료됨' 상태로 업데이트
        int remainingClearTime = challengeMapper.getTotalClearTime(challengeVerifyDTO.getChallIdx());
        if (remainingClearTime <= 0) {
            challengeMapper.finishChallenge(challengeVerifyDTO.getChallIdx());
        }
    }

    // 챌린지 하루 한번 인증
    public boolean hasAlreadyVerifiedToday(Long userIdx, Long challIdx) {
        LocalDate today = LocalDate.now();
//        LocalDateTime startOfDay = today.atStartOfDay();
//        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        return challengeMapper.countTodayVerification(userIdx, challIdx, today) > 0;
    }



    // 챌린지 존재 여부 확인 (서비스 구현 수정)
    @Override
    public boolean existsById(Long challIdx) {
        return challengeMapper.existsById(challIdx) > 0;  // 0보다 크면 존재하는 챌린지
    }


    // 챌린지 검색
    public List<ChallengeDTO> searchChallenges(String challTitle, Integer challCategoryIdx, int offset, int size, String sort) {
        return challengeMapper.searchChallenges(challTitle, challCategoryIdx, offset, size, sort);
    }
}

