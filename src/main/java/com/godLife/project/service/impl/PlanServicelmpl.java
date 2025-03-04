package com.godLife.project.service.impl;

import com.godLife.project.dto.categories.JobEtcCateDTO;
import com.godLife.project.dto.datas.ActivityDTO;
import com.godLife.project.dto.datas.PlanDTO;
import com.godLife.project.mapper.PlanMapper;
import com.godLife.project.service.PlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Slf4j
@Service
public class PlanServicelmpl implements PlanService {

  private final PlanMapper planMapper;

  public PlanServicelmpl(PlanMapper planMapper) {
    this.planMapper = planMapper;
  }

  // 루틴 작성 로직
  @Override
  @Transactional(rollbackFor = Exception.class)
  public int insertPlanWithAct(PlanDTO planDTO) {
    try {
      //System.out.println(planDTO);
      // 루틴 삽입하기
      planMapper.insertPlan(planDTO);

      int planIdx = planDTO.getPlanIdx();

      // 해당 루틴의 활동 삽입
      for (ActivityDTO activityDTO : planDTO.getActivities()) {
        activityDTO.setPlanIdx(planIdx);
        planMapper.insertActivity(activityDTO);
      }

      if (planDTO.getJobIdx() == 999) {
        JobEtcCateDTO jobEtcCateDTO = new JobEtcCateDTO();
        jobEtcCateDTO = planDTO.getJobEtcCateDTO();
        jobEtcCateDTO.setPlanIdx(planIdx);

        planMapper.insertJobEtc(jobEtcCateDTO);
      }
      return 200;
    } catch (Exception e) {
      log.error("e: ", e);
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 수동 롤백
      return 500;
    }
  }

  // 루틴 상세 보기 로직
  @Override
  @Transactional
  public PlanDTO detailRoutine(int planIdx, int isDeleted) {
    // 루틴 조회
    PlanDTO planDTO = planMapper.detailPlanByPlanIdx(planIdx, isDeleted);
    if (planDTO != null) {
      // 활동 조회
      planDTO.setActivities(planMapper.detailActivityByPlanIdx(planIdx));
      // 관심사 정보 조회
      planDTO.setTargetCateDTO(planMapper.getTargetCategoryByTargetIdx(planDTO.getTargetIdx()));

      if (planDTO.getJobIdx() == 999) {
        planDTO.setJobEtcCateDTO(planMapper.getJobEtcInfoByPlanIdx(planIdx));
      }
      else {
        planDTO.setJobCateDTO(planMapper.getJOBCategoryByJobIdx(planDTO.getJobIdx()));
      }
      return planDTO;
    }
    return null;
  }

  // 루틴 수정 로직
  @Override
  @Transactional(rollbackFor = Exception.class)
  public int modifyPlanWithAct(PlanDTO planDTO, int isDeleted) {
    int planIdx = planDTO.getPlanIdx();
    int userIdx = planDTO.getUserIdx();

    // 루틴 존재 여부 확인
    if (!planMapper.checkPlanByPlanIdx(planIdx, isDeleted)) {
      return 404; // Not Found
    }
    // 작성자만 수정 가능
    if (userIdx != planMapper.getUserIdxByPlanIDx(planIdx)) {
      return 403; // Forbidden
    }

    try {
      // 루틴 수정
      planMapper.modifyPlan(planDTO);
      // 삭제할 활동 처리
      deleteActivities(planDTO.getDeleteActivityIdx());
      // 활동 수정 및 추가
      processActivities(planIdx, planDTO.getActivities());
      // 기타 직업 수정 및 추가
      processJobEtc(planIdx, planDTO.getJobIdx(), planDTO.getJobEtcCateDTO());

      return 200; // OK
    } catch (Exception e) {
      log.error("Error modifying plan: ", e);
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 롤백
      return 500; // Internal Server Error
    }
  }
  // 삭제할 활동 처리 함수
  private void deleteActivities(List<Integer> deleteActivityIdx) {
    if (deleteActivityIdx != null) {
      deleteActivityIdx.forEach(planMapper::deleteActByActivityIdx);
    }
  }
  // 활동 수정 및 추가 함수
  private void processActivities(int planIdx, List<ActivityDTO> activities) {
    for (ActivityDTO activityDTO : activities) {
      activityDTO.setPlanIdx(planIdx);
      int activityIdx = activityDTO.getActivityIdx();
      if (planMapper.checkActByActivityIdx(planIdx, activityIdx)) {
        planMapper.modifyActivity(activityDTO); // 수정
      } else {
        planMapper.insertActivity(activityDTO); // 추가
      }
    }
  }
  // 기타 직업 수정 및 추가 함수
  private void processJobEtc(int planIdx, int jobIdx, JobEtcCateDTO jobEtcCateDTO) {
    if (jobIdx != 999) { // 999 선택 안할 시 로직 건너뜀.
      System.out.println("기타 직업 삽입 무시함.");
      return;
    }
    jobEtcCateDTO.setPlanIdx(planIdx);
    if (planMapper.checkJobEtcByPlanIdx(planIdx)) {
      System.out.println("기타 존재함.. 수정 로직 실행");
      planMapper.modifyJobEtc(jobEtcCateDTO); // true => 기타 직업 존재,, 수정 로직 실행
    } else {
      System.out.println("기타 직업 없음.. 추가 로직 실행");
      planMapper.insertJobEtc(jobEtcCateDTO); // false => 기타 직업 없음,, 삽입 로직 실행
    }
  }


  @Override
  public int deletePlan(int planIdx, int userIdx) {
    int isDeleted = 0;
    if (!planMapper.checkPlanByPlanIdx(planIdx, isDeleted)) {
      return 404; // not found
    }
    if (planMapper.getUserIdxByPlanIDx(planIdx) != userIdx) {
      return 403; // another
    }
    planMapper.deletePlan(planIdx, userIdx);
    return 200; // ok
  }

}
