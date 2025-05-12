package com.godLife.project.mapper;

import com.godLife.project.dto.list.PlanListDTO;
import com.godLife.project.dto.list.QnaListDTO;
import com.godLife.project.dto.list.customDTOs.CustomPlanDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ListMapper {

  // 로그인한 유저의 진행/대기 중 루틴들 조회
  List<CustomPlanDTO> getMyPlansByUserIdx(int userIdx);
  // 루틴의 목표(관심사) 인덱스 번호 조회
  @Select("SELECT TARGET_IDX FROM PLAN_TABLE WHERE PLAN_IDX = #{planIdx}")
  int getTargetIdxByPlanIdx(int plansIdx);
  // 루틴의 직업 인덱스 번호 조회
  @Select("SELECT JOB_IDX FROM PLAN_TABLE WHERE PLAN_IDX = #{planIdx}")
  int getJobIdxByPlanIdx(int plansIdx);

  // 루틴의 리스트 정보 조회
  List<PlanListDTO> getAllPlanList(@Param("mode") String mode,
                                   @Param("offset") int offset,
                                   @Param("size") int size,
                                   @Param("status") int status,
                                   @Param("target") List<Integer> target,
                                   @Param("job") List<Integer> job,
                                   @Param("sort") String sort,
                                   @Param("order") String order,
                                   @Param("keywords") Map<String, List<String>> keywords,
                                   @Param("userIdx") int userIdx);
  // 좋아요 한 루틴 리스트 조회
  List<PlanListDTO> getLikePlanList(@Param("mode") String mode,
                                   @Param("offset") int offset,
                                   @Param("size") int size,
                                   @Param("target") List<Integer> target,
                                   @Param("job") List<Integer> job,
                                   @Param("order") String order,
                                   @Param("keywords") Map<String, List<String>> keywords,
                                   @Param("userIdx") int userIdx);

  // 루틴 리스트 총 게시글 수 조회
  int getTotalPlanCount(@Param("mode") String mode,
                        @Param("status") int status,
                        @Param("target") List<Integer> target,
                        @Param("job") List<Integer> job,
                        @Param("keywords") Map<String, List<String>> keywords,
                        @Param("userIdx") int userIdx);

  /**
   * 문의 리스트 조회
   * @param qUserIdx 문의 리스트를 조회할 유저의 인덱스 번호
   * @param notStatus 조회할 때 제외할 문의의 상태
   * @param offset 오프셋
   * @param size 조회 할 문의 개수
   * @return {@code List<QnaListDTO>}
   */
  List<QnaListDTO> getQnaList(@Param("qUserIdx") int qUserIdx,
                              @Param("notStatus") String notStatus,
                              @Param("offset") int offset,
                              @Param("size") int size,
                              @Param("status") String status,
                              @Param("sort") String sort,
                              @Param("order") String order,
                              @Param("keywords") Map<String, List<String>> keywords);

  /**
   * 조회 할 문의 리스트의 총 문의 수
   * @param qUserIdx 문의 리스트를 조회할 유저의 인덱스 번호
   * @param notStatus 조회할 때 제외할 문의의 상태
   * @return {@code int}
   */
  int getTotalQnaCount(@Param("qUserIdx") int qUserIdx,
                       @Param("notStatus") String notStatus,
                       @Param("status") String status,
                       @Param("keywords") Map<String, List<String>> keywords);

}
