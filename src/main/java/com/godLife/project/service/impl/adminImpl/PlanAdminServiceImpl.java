package com.godLife.project.service.impl.adminImpl;

import com.godLife.project.dto.categories.JobEtcCateDTO;
import com.godLife.project.dto.datas.ActivityDTO;
import com.godLife.project.dto.datas.PlanDTO;
import com.godLife.project.dto.list.customDTOs.CustomAdminPlanListDTO;
import com.godLife.project.mapper.AdminMapper.PlanAdminMapper;
import com.godLife.project.service.interfaces.AdminInterface.PlanAdminService;
import com.godLife.project.service.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Slf4j
@Service
public class PlanAdminServiceImpl implements PlanAdminService {

  private final PlanAdminMapper planAdminMapper;

  public PlanAdminServiceImpl(PlanAdminMapper planAdminMapper){this.planAdminMapper = planAdminMapper;}

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

  // 루틴 작성 로직
  @Transactional(rollbackFor = Exception.class)
  public int adminInsertPlan(PlanDTO planDTO) {
    try {
      int userIdx = planDTO.getUserIdx();
      int isCompleted = planDTO.getIsCompleted(); // 0
      int isDeleted = planDTO.getIsDeleted();     // 0
      if (planAdminMapper.getCntOfPlanByUserIdxNIsCompleted(userIdx, isCompleted, isDeleted) > 4) {
        return 412;
      }
      //System.out.println(planDTO);
      if (!planAdminMapper.getUserIsDeleted(userIdx).contains("N")) { return 410; }
      // 루틴 삽입하기
      planAdminMapper.adminInsertPlan(planDTO);

      int planIdx = planDTO.getPlanIdx();

      // 해당 루틴의 활동 삽입
      for (ActivityDTO activityDTO : planDTO.getActivities()) {
        activityDTO.setPlanIdx(planIdx);
        planAdminMapper.adminInsertActivity(activityDTO);
      }

      if (planDTO.getJobIdx() == 999) {
        JobEtcCateDTO jobEtcCateDTO = new JobEtcCateDTO();
        jobEtcCateDTO = planDTO.getJobEtcCateDTO();
        jobEtcCateDTO.setPlanIdx(planIdx);

        planAdminMapper.insertJobEtc(jobEtcCateDTO);
      }

      if (planDTO.isForked()) {
        int forkIdx = planDTO.getForkIdx();
        planAdminMapper.modifyForkCount(forkIdx, isDeleted); // fork 하여 루틴 작성시 원본 루틴의 포크수 증가
      }

      return 201;
    } catch (Exception e) {
      log.error("e: ", e);
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 수동 롤백
      return 500;
    }
  }
  }
