package com.godLife.project.mapper;

import com.godLife.project.dto.contents.ChallengeDTO;
import com.godLife.project.dto.infos.VerifyDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChallengeMapper {
        // 챌린지 생성
    void createChallenge(ChallengeDTO challengeDTO);

    // 최신 챌린지 리스트 조회
    List<ChallengeDTO> getLatestChallenges();

    // 챌린지 정보 조회
    ChallengeDTO getChallenge(Long challIdx);
    // 챌린지 시작 업데이트 (최초 참여시)
    void startChallenge(ChallengeDTO challenge);
    // 인증 기록 저장
    void insertVerifyRecord(VerifyDTO verifyDTO);
    // 남은 클리어시간 차감
    void updateClearTime(@Param("challIdx") Long challIdx, @Param("elapsedTime") int elapsedTime);
    // 클리어 시간 0 이하 시 챌린지 종료
    void finishChallenge(Long challIdx);


}
