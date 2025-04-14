package com.godLife.project.service.interfaces;

import com.godLife.project.dto.contents.ChallengeDTO;
import com.godLife.project.dto.verify.ChallengeVerifyDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChallengeService {
    // 최신 챌린지 조회 (페이징 적용)
    List<ChallengeDTO> getLatestChallenges(int page, int size);

    // 최신 챌린지 총 개수 조회
    int getTotalLatestChallenges();

    // 카테고리별 챌린지 조회 (페이징 적용)
    List<ChallengeDTO> getChallengesByCategoryId(int categoryIdx, int page, int size);

    // 카테고리별 챌린지 총 개수 조회
    int getTotalChallengesByCategory(int categoryIdx);

    // 챌린지 생성
    int createChallenge(ChallengeDTO challengeDTO);

    //챌린지 상세 조회
    ChallengeDTO getChallengeDetail(Long challIdx);

    // 챌린지 참가
    ChallengeDTO joinChallenge(Long challIdx, int userIdx, String activity, int activityTime);

    // 챌린지 인증
    void verifyChallenge(ChallengeVerifyDTO challengeVerifyDTO);

    // 챌린지 존재 여부
    boolean existsById(Long challIdx);
    // 챌린지 수정
    int modifyChallenge(ChallengeDTO challengeDTO);
    // 챌린지 삭제
    int deleteChallenge(ChallengeDTO challengeDTO);

    // 챌린지 검색 (제목, 카테고리)
    List<ChallengeDTO> searchChallenges(String challTitle,
                                        String challCategory,
                                        int offset, int size, String sort);


}
