package com.godLife.project.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChallengeJoinMapper {
    // 챌린지 참여 여부 확인
    boolean isUserJoined(@Param("userId") Long userId, @Param("challIdx") int challIdx);

    // 현재 참여 인원 조회
    int getCurrentParticipants(@Param("challIdx") int challIdx);

    // 챌린지 참여 추가
    void insertChallengeJoin(@Param("userId") Long userId, @Param("challIdx") int challIdx);
}
