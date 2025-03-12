package com.godLife.project.service.interfaces;

import com.godLife.project.dto.contents.ChallengeDTO;
import com.godLife.project.dto.infos.VerifyDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface ChallengeService {
    // 최신 챌린지 리스트
    List<ChallengeDTO> getLatestChallenges();

    // 카테고리별 챌린지 조회
    List<ChallengeDTO> getChallengesByCategoryId(int categoryIdx);

    // 챌린지 생성
    void createChallenge(ChallengeDTO challengeDTO) throws Exception;

    void joinChallenge(Long challIdx, int userIdx, LocalDateTime challEndTime);

    void verifyChallenge(VerifyDTO verifyDTO, ChallengeDTO challengeDTO);

    // 챌린지 수정
    void modifyChallenge(ChallengeDTO challengeDTO);
    // 챌린지 삭제
    void deleteChallenge(Long challIdx);
}
