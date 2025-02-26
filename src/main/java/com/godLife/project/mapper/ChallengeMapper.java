package com.godLife.project.mapper;

import com.godLife.project.dto.contents.ChallengeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ChallengeMapper {
        // 챌린지 생성
    void createChallenge(ChallengeDTO challengeDTO);

        // 챌린지 시작 시간 및 종료 시간 업데이트
    void updateChallengeStartTime(
          @Param("challIdx") int challIdx,
          @Param("startTime") LocalDateTime startTime,
          @Param("endTime") LocalDateTime endTime
    );

        // 최신 챌린지 리스트 조회
    List<ChallengeDTO> getLatestChallenges();

        // 챌린지 상태 업데이트 (게시중 → 진행중)
    void updateChallengeState(
        @Param("challIdx") int challIdx,
        @Param("state") String state
    );
    // 챌린지 ID로 특정 챌린지 조회
    @Select("SELECT * FROM CHALL_TABLE WHERE chall_idx = #{challIdx}")
    ChallengeDTO getChallengeById(@Param("challIdx") int challIdx);
    }
