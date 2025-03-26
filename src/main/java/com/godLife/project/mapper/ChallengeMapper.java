package com.godLife.project.mapper;

import com.godLife.project.dto.contents.ChallengeDTO;
import com.godLife.project.dto.infos.VerifyDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface ChallengeMapper {
    // 챌린지 생성
    void createChallenge(ChallengeDTO challengeDTO);

    // 최신 챌린지 조회 (페이징 적용)
    List<ChallengeDTO> getLatestChallenges(@Param("offset") int offset, @Param("size") int size);

    // 최신 챌린지 총 개수 조회
    int countLatestChallenges();

    // 카테고리별 챌린지 조회 (페이징 적용)
    List<ChallengeDTO> getChallengesByCategoryId(@Param("challCategoryIdx") int challCategoryIdx,
                                                 @Param("offset") int offset,
                                                 @Param("size") int size);

    // 카테고리별 챌린지 총 개수 조회
    int countChallengesByCategoryId(@Param("challCategoryIdx") int challCategoryIdx);

    // 챌린지 상세조회
    ChallengeDTO challengeDetail(Long challIdx);

    // 현재 참여자수 조회
    int countParticipants(Long challIdx);

    // 유저 참여형 챌린지 최초 참여시 시작/종료시간, 상태 업데이트
    void updateChallengeStartTime(Map<String, Object> params);
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
    // 챌린지 존재 여부 확인
    int existsById(@Param("challIdx") Long challIdx);
    // 챌린지 수정
    int modifyChallenge(ChallengeDTO challengeDTO);
    // 챌린지 삭제
    int deleteChallenge(ChallengeDTO challengeDTO);

    // 인증을 통해 감소한 총 시간 조회
    Integer getElapsedClearTime(@Param("challIdx") Long challIdx);

    // 사용자가 해당 챌린지에 참여 중인지 확인
    boolean isUserJoined(@Param("challIdx") Long challIdx, @Param("userId") Long userId);

    // 챌린지의 시작 시간을 가져오는 메서드
    LocalDateTime getChallengeStartTime(Long challIdx);

}
