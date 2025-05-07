package com.godLife.project.mapper.AdminMapper;

import com.godLife.project.dto.list.customDTOs.CustomAdminPlanListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlanAdminMapper {
  // 관리자 루틴 리스트 조회 (페이징 처리)
  List<CustomAdminPlanListDTO> selectAdminPlanList(@Param("offset") int offset, @Param("limit") int limit);
  // 전체 루틴 개수 조회
  int getTotalAdminPlanCount();
  // 관리자 루틴 카테고리 조회 (페이징 처리)
  List<CustomAdminPlanListDTO> selectAdminPlanListByTargetIdx(
          @Param("targetIdx") Integer targetIdx,
          @Param("offset") int offset, @Param("limit") int limit);
  List<CustomAdminPlanListDTO> selectPlanList (@Param("offset") int offset, @Param("limit") int limit);

}
