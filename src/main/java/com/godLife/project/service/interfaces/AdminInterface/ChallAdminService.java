package com.godLife.project.service.interfaces.AdminInterface;

import com.godLife.project.dto.contents.ChallengeDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChallAdminService {
  // 챌린지 생성
  int createChallenge(ChallengeDTO challengeDTO);
  // 챌린지 존재 여부
  boolean existsById(Long challIdx);
  // 챌린지 수정
  int modifyChallenge(ChallengeDTO challengeDTO);
  // 챌린지 삭제
  int deleteChallenge(@Param("challIdx") Long challIdx);
  // 조기 종료
  void earlyFinishChallenge(Long challIdx);

  // 챌린지 검색 (제목, 카테고리)
  List<ChallengeDTO> searchChallenges(String challTitle,
                                      String challCategory,
                                      int offset, int size, String sort);

  List<ChallengeDTO> getAllChallengesAdmin(int page, int size);    // 최신 챌린지 조회 (페이징 적용)

  int getTotalLatestChallenges();         // 최신 챌린지 총 개수 조회


  List<ChallengeDTO> getChallengesByCategoryId(int categoryIdx, int page, int size);   // 카테고리별 챌린지 조회 (페이징 적용)

  int getTotalChallengesByCategory(int categoryIdx);        // 카테고리별 챌린지 총 개수 조회

  ChallengeDTO getChallengeDetail(Long challIdx);          // 챌린지 상세 조회

}
