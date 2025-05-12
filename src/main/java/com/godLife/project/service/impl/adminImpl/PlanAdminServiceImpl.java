package com.godLife.project.service.impl.AdminImpl;

import com.godLife.project.dto.list.customDTOs.CustomAdminPlanListDTO;
import com.godLife.project.mapper.AdminMapper.PlanAdminMapper;
import com.godLife.project.service.interfaces.AdminInterface.PlanAdminService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanAdminServiceImpl implements PlanAdminService {

  private PlanAdminMapper planAdminMapper;

  public PlanAdminServiceImpl(PlanAdminMapper planAdminMapper){
    this.planAdminMapper = planAdminMapper;
  }

  // 관리자 루틴 리스트 조회 (페이징 처리)
  @Override
  public List<CustomAdminPlanListDTO> selectAdminPlanList(int page, int size) {
    // 페이징 처리 계산
    int offset = (page - 1) * size;
    int limit = size;
    return planAdminMapper.selectAdminPlanList(offset, limit);
  }
  // 전체 관리자 루틴 개수 조회
  @Override
  public int getTotalAdminPlanCount() {
    return planAdminMapper.getTotalAdminPlanCount();
  }

  // 관리자 루틴 카테고리 조회
  public List<CustomAdminPlanListDTO> selectAdminPlanListByTargetIdx(
          Integer targetIdx, int page, int size){
    // 페이징 처리 계산
    int offset = (page - 1) * size;
    int limit = size;
    return planAdminMapper.selectAdminPlanListByTargetIdx(targetIdx, offset, limit);
  }

  // 루틴 전체 조회
  @Override
  public List<CustomAdminPlanListDTO> selectPlanList(int page, int size) {
    // 페이징 처리 계산
    int offset = (page - 1) * size;
    int limit = size;
    return planAdminMapper.selectPlanList(offset, limit);
  }


}
