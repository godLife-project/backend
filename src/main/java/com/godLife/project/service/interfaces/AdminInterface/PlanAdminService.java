package com.godLife.project.service.interfaces.AdminInterface;

import com.godLife.project.dto.datas.PlanDTO;
import com.godLife.project.dto.list.customDTOs.CustomAdminPlanListDTO;
import com.godLife.project.mapper.AdminMapper.PlanAdminMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlanAdminService {
  // 관리자 루틴 리스트 조회 (페이징 처리)
  List<CustomAdminPlanListDTO> selectAdminPlanList(int page, int size);
  // 전체 관리자 루틴 개수 조회
  int getTotalAdminPlanCount();
  // 관리자 루틴 카테고리 조회 (페이징 처리)
  List<CustomAdminPlanListDTO> selectAdminPlanListByTargetIdx(
          @Param("targetIdx") Integer targetIdx,
          @Param("offset") int offset, @Param("limit") int limit);

  List<CustomAdminPlanListDTO> selectPlanList (@Param("offset") int offset, @Param("limit") int limit);

}
