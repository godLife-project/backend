package com.godLife.project.service.interfaces;

import com.godLife.project.dto.contents.ChallengeDTO;
import com.godLife.project.dto.infos.VerifyDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface ChallengeService {
    // 최신 챌린지 리스트
    List<ChallengeDTO> getLatestChallenges();

    // 카테고리별 챌린지 조회
    List<ChallengeDTO> getChallengesByCategoryId(int categoryIdx);

    // 챌린지 생성
    void createChallenge(ChallengeDTO challengeDTO) throws Exception;
    // 유저 참여형 / 시작시간 업데이트
    void updateChallengeStartTime(Long challIndx, Integer duration) throws Exception;

    //챌린지 상세 조회
    ChallengeDTO getChallnegeDetail(Long challIdx, int userJoin, Integer duration);

    // 챌린지 참가
    ChallengeDTO joinChallenge(Long challIdx, int userIdx, Integer duration,
                               LocalDateTime startTime, LocalDateTime endTime, String activity);

    void verifyChallenge(VerifyDTO verifyDTO, ChallengeDTO challengeDTO);

    // 챌린지 수정
    void modifyChallenge(ChallengeDTO challengeDTO);
    // 챌린지 삭제
    void deleteChallenge(Long challIdx);


}
