package com.godLife.project.mapper;

import com.godLife.project.dto.contents.ChallengeDTO;
import com.godLife.project.dto.infos.ChallengeJoinDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface ChallengeMapper {

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

    // 상세조회시 참가자 조회
    List<ChallengeJoinDTO> getChallengeParticipants(Long challIdx);

    // 현재 참여자수 조회
    int countParticipants(Long challIdx);

    // 유저 참여형 챌린지 최초 참여시 시작/종료시간, 상태 업데이트
    void updateChallengeStartTime(Map<String, Object> params);
    // 챌린지에 유저 추가 (사용자 참여 기록)
    void addUserToChallenge(@Param("challIdx") Long challIdx,
                            @Param("userIdx") int userIdx,
                            @Param("activity") String activity,
                            @Param("activityTime") int activityTime);


    // 인증 기록 저장
    int insertVerify(
            @Param("challIdx") Long challIdx,
            @Param("userIdx") Long userIdx,
            @Param("elapsedTime") Long elapsedTime
    );
    // 활동 정보 갱신
    int updateChallJoin(
            @Param("challIdx") Long challIdx,
            @Param("userIdx") int userIdx,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("activity") String activity
    );
    // 챌린지 참여 확인 및 중복 체크
    ChallengeJoinDTO getJoinInfo(Long challIdx, Long userIdx);

    // 하루 한번 인증
    int countTodayVerification(@Param("userIdx") Long userIdx,
                               @Param("challIdx") Long challIdx,
                               @Param("startOfDay") LocalDateTime startOfDay,
                               @Param("endOfDay") LocalDateTime endOfDay);
    // 남은 클리어시간 차감
    void updateClearTime(@Param("challIdx") Long challIdx, @Param("elapsedTime") int elapsedTime);
    // 클리어 시간 0 이하 시 챌린지 종료
    void finishChallenge(Long challIdx);
    // 총 클리어 시간 확인
    int getTotalClearTime(Long challIdx);

    // 챌린지 상태 업데이트
    int updateChallengesToCompleted();


    // 챌린지 존재 여부 확인
    int existsById(@Param("challIdx") Long challIdx);

    // 인증을 통해 감소한 총 시간 조회
    Integer getElapsedClearTime(@Param("challIdx") Long challIdx);

    // 챌린지 검색 (제목, 카테고리)
    List<ChallengeDTO> searchChallenges(
            @Param("challTitle") String challTitle,
            @Param("challCategory") String challCategory,
            @Param("offset") int offset,
            @Param("size") int size,
            @Param("sort") String sort
    );
    // 진행 중인 챌린지를 종료 상태로 자동 변경
    void updateChallengesToEndStatus(@Param("now") LocalDateTime now);



}
