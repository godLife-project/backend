package com.godLife.project.mapper;

import com.godLife.project.dto.contents.ChallengeDTO;
import com.godLife.project.dto.infos.VerifyDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ChallengeMapper {
    // 챌린지 생성
    void createChallenge(ChallengeDTO challengeDTO);

    // 최신 챌린지 리스트 조회
    List<ChallengeDTO> getLatestChallenges();

    // 카테고리별 챌린지 조회
    List<ChallengeDTO> getChallengesByCategoryId(@Param("categoryIdx") int categoryIdx);

    // 챌린지 상세조회
    ChallengeDTO ChallengeDetail(Long challIdx);

    // 현재 참여자수 조회
    int countParticipants(Long challIdx);

    // 유저 참여형 챌린지 최초 참여시 시작/종료시간, 상태 업데이트
    void updateChallengeStartTime(@Param("challIdx") Long challIdx,
                                          @Param("startTime") LocalDateTime challStartTime,
                                          @Param("endTime") LocalDateTime challEndTime,
                                          @Param("state") String state);


    // 챌린지에 유저 추가 (사용자 참여 기록)
    void addUserToChallenge(@Param("challIdx") Long challIdx,
                            @Param("userIdx") int userIdx,
                            @Param("startTime") LocalDateTime startTime,
                            @Param("endTime") LocalDateTime endTime,
                            @Param("activity")  String activity);
    // 인증 기록 저장
    void insertVerifyRecord(VerifyDTO verifyDTO);
    // 남은 클리어시간 차감
    void updateClearTime(@Param("challIdx") Long challIdx, @Param("elapsedTime") int elapsedTime);
    // 클리어 시간 0 이하 시 챌린지 종료
    void finishChallenge(Long challIdx);
    // 챌린지 수정
    int modifyChallenge(ChallengeDTO challengeDTO);
    // 챌린지 삭제
    int deleteChallenge(Long challIdx);

}
