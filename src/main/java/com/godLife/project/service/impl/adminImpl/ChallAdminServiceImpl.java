package com.godLife.project.service.impl.adminImpl;

import com.godLife.project.dto.contents.ChallengeDTO;
import com.godLife.project.dto.infos.ChallengeJoinDTO;
import com.godLife.project.enums.ChallengeState;
import com.godLife.project.mapper.AdminMapper.ChallAdminMapper;
import com.godLife.project.service.interfaces.AdminInterface.ChallAdminService;
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
@Transactional
public class ChallAdminServiceImpl implements ChallAdminService {
  private final ChallAdminMapper challAdminMapper;

  public ChallAdminServiceImpl(ChallAdminMapper challAdminMapper) {
    this.challAdminMapper = challAdminMapper;
  }

  // ----------------- 챌린지 작성 -----------------
  @Override
  @Transactional(rollbackFor = Exception.class)
  public int createChallenge(ChallengeDTO challengeDTO) {
    try {
      // 관리자 개입형 처리
      if (challengeDTO.getUserJoin() == 0) {
        if (challengeDTO.getDuration() == null || challengeDTO.getDuration() <= 0) {  // 0 이하일 때 예외 발생
          throw new IllegalArgumentException("관리자 개입형 챌린지는 유효한 기간(Duration)이 필요합니다. (0 이상의 값으로 설정하세요)");
        }

        challengeDTO.setChallState(ChallengeState.IN_PROGRESS.getCode());

        // 현재 시간을 시작 시간으로 설정
        LocalDateTime startTime = LocalDateTime.now();
        challengeDTO.setChallStartTime(startTime);

        // 종료 시간 설정
        Integer duration = challengeDTO.getDuration();
        LocalDateTime endTime = calculateEndTime(startTime, duration);

        challengeDTO.setChallEndTime(endTime);
      }
      // 유저 참여형 처리
      else if (challengeDTO.getUserJoin() == 1) {
        challengeDTO.setChallState(ChallengeState.PUBLISHED.getCode());
        challengeDTO.setChallStartTime(null); // 참가자가 생길 때 설정
        challengeDTO.setChallEndTime(null);
      } else {
        throw new IllegalArgumentException("userJoin 값은 0 또는 1이어야 합니다.");
      }

      // 챌린지 생성
      challAdminMapper.createChallenge(challengeDTO);
      return 201;
    } catch (Exception e) {
      log.error("e: ", e);
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      return 500;
    }
  }

  // 종료 시간 계산 로직
  private LocalDateTime calculateEndTime(LocalDateTime challStartTime, int duration) {
    return challStartTime.plusDays(duration);
  }

  // 챌린지 존재 여부 확인 (서비스 구현 수정)
  @Override
  public boolean existsById(Long challIdx) {
    return challAdminMapper.existsById(challIdx) > 0;  // 0보다 크면 존재하는 챌린지
  }


  // ----------------- 챌린지 수정 -----------------
  @Override
  public int modifyChallenge(ChallengeDTO challengeDTO) {
    int updatedCount = challAdminMapper.modifyChallenge(challengeDTO);
    // 1건만 수정이 되는지 확인
    if (updatedCount != 1) {
      throw new IllegalArgumentException("챌린지 수정 실패");
    }
    return updatedCount;
  }

  // ----------------- 챌린지 삭제 -----------------
  @Transactional
  public int deleteChallenge(Long challIdx) {
    // 삭제 수행
    challAdminMapper.deleteVerifyByChallIdx(challIdx); // 자식 테이블 먼저 삭제 (인증테이블)
    challAdminMapper.deleteChallJoinByChallIdx(challIdx);  // 자식 테이블 먼저 삭제 (조인테이블)
    int result = challAdminMapper.deleteChallenge(challIdx);        // 부모 테이블 삭제
    if (result == 0) {
      return 500;
    }
    return 200; // 삭제 성공
  }


  // 챌린지 검색
  public List<ChallengeDTO> searchChallenges(String challTitle, String challCategory, int offset, int size, String sort) {
    return challAdminMapper.searchChallenges(challTitle, challCategory, offset, size, sort);
  }



  // --------------------- 조회 -----------------------
  // ----------------- 최신 챌린지 조회 (페이징 적용) -----------------
  @Override
  public List<ChallengeDTO> getAllChallengesAdmin(int page, int size) {
    int offset = (page - 1) * size;
    return challAdminMapper.getAllChallengesAdmin(offset, size);
  }
  // 최신 챌린지 총 개수 조회
  @Override
  public int getTotalLatestChallenges() {
    return challAdminMapper.countLatestChallenges();
  }


  // -----------------카테고리별 챌린지 조회 (페이징 적용) -----------------
  @Override
  public List<ChallengeDTO> getChallengesByCategoryId(int categoryIdx, int page, int size) {
    int offset = (page - 1) * size;
    return challAdminMapper.getChallengesByCategoryId(categoryIdx, offset, size);
  }
  // 카테고리별 챌린지 총 개수 조회
  @Override
  public int getTotalChallengesByCategory(int categoryIdx) {
    return challAdminMapper.countChallengesByCategoryId(categoryIdx);
  }


  // ----------------- 챌린지 상세 조회 -----------------
  public ChallengeDTO getChallengeDetail(Long challIdx) {
    // 챌린지 기본 정보 조회
    ChallengeDTO challenge = challAdminMapper.challengeDetail(challIdx);
    if (challenge == null) {
      throw new IllegalArgumentException("해당 챌린지를 찾을 수 없습니다: challIdx=" + challIdx);
    }

    // 현재 참여자 수
    int participantCount = challAdminMapper.countParticipants(challIdx);
    challenge.setCurrentParticipants(participantCount);


    // 인증을 통해 감소된 클리어 시간 반영
    int elapsedClearTime = challAdminMapper.earlyFinishChallenge(challIdx); // 인증을 통해 사용된 시간 조회
    int updatedClearTime = Math.max(0, challenge.getTotalClearTime() - elapsedClearTime); // 음수 방지
    challenge.setTotalClearTime(updatedClearTime); // 인증을 통한 총 클리어시간 감소 조회

    // 종료 시간이 되면 상태를 "완료됨"으로 변경
    if (challenge.getChallEndTime() != null && LocalDateTime.now().isAfter(challenge.getChallEndTime())
            && !challenge.getChallState().equals(ChallengeState.COMPLETED.getCode())) {
      challenge.setChallState(ChallengeState.COMPLETED.getCode());
    }

    // 참가자 상세 정보 조회 및 설정
    List<ChallengeJoinDTO> participants = challAdminMapper.getChallengeParticipants(challIdx);
    challenge.setParticipants(participants);

    // DB 상태 업데이트 (최소화된 한 번의 업데이트)
    Map<String, Object> params = new HashMap<>();
    params.put("challIdx", challIdx);
    params.put("challStartTime", challenge.getChallStartTime());
    params.put("challEndTime", challenge.getChallEndTime());
    params.put("challState", challenge.getChallState());
    params.put("totalClearTime", challenge.getTotalClearTime());

    challAdminMapper.updateChallengeStartTime(params);

    return challenge;
  }

  // 조기종료
  @Transactional
  public void earlyFinishChallenge(Long challIdx) {
    int updated = challAdminMapper.earlyFinishChallenge(challIdx);
    if (updated == 0) {
      throw new IllegalStateException("이미 종료된 챌린지이거나 존재하지 않습니다. challIdx = " + challIdx);
    }
  }
}
