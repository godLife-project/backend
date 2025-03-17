package com.godLife.project.service.impl;

import com.godLife.project.dto.list.MyPlanDTO;
import com.godLife.project.dto.list.PlanListDTO;
import com.godLife.project.dto.list.customDTOs.CustomPlanDTO;
import com.godLife.project.mapper.ListMapper;
import com.godLife.project.mapper.PlanMapper;
import com.godLife.project.service.interfaces.ListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListServiceImpl implements ListService {

  public final ListMapper listMapper;
  public final PlanMapper planMapper;

  @Override
  @Transactional
  public List<MyPlanDTO> getMyPlansList(int userIdx) {
    List<MyPlanDTO> myPlanList = new ArrayList<>();
    try {
      List<CustomPlanDTO> planDtos = listMapper.getMyPlansByUserIdx(userIdx);

      if (planDtos == null || planDtos.isEmpty()) {
        return new ArrayList<>(); // 조회된 루틴 없을 때, 빈 리스트 보내기
      }

      for (CustomPlanDTO planDTO : planDtos) {
        MyPlanDTO myPlanDTO = new MyPlanDTO();
        int planIdx = planDTO.getPlanIdx();
        int targetIdx = listMapper.getTargetIdxByPlanIdx(planDTO.getPlanIdx());
        int jobIdx = listMapper.getJobIdxByPlanIdx(planIdx);

        myPlanDTO.setMyPlanInfos(planDTO); // 루틴 정보 추가
        myPlanDTO.setMyActivities(planMapper.detailActivityByPlanIdx(planIdx)); // 활동 정보 추가
        myPlanDTO.setTargetInfos(planMapper.getTargetCategoryByTargetIdx(targetIdx)); // 목표(관심사) 정보 추가
        myPlanDTO.setFireInfos(planMapper.detailFireByPlanIdx(planIdx)); // 불꽃 레벨 정보 추가

        // 직업 정보 추가
        if (jobIdx == 999) {
          myPlanDTO.setJobAddedInfos(planMapper.getJobEtcInfoByPlanIdx(planIdx));
        }
        else {
          myPlanDTO.setJobDefaultInfos(planMapper.getJOBCategoryByJobIdx(jobIdx));
        }

        // 리스트 추가
        myPlanList.add(myPlanDTO);
      }
      return myPlanList;
    } catch (Exception e) {
      log.error("e: ", e);
      return null; // 서버 에러 발생시 null 보내기
    }
  }

  @Override
  public Map<String, Object> getAllPlansList(String mode, int page, int size, int status,
                                             List<Integer> target, List<Integer> job,
                                             String sort, String order) {
    // 페이지 번호가 음수일 경우 예외 처리
    if (page < 0) {
      Map<String, Object> errorResponse = new HashMap<>();
      errorResponse.put("error", "유효하지 않은 페이지 번호");
      errorResponse.put("message", "페이지 번호는 1부터 시작 되어야 합니다.");
      return errorResponse;
    }
    try {
      int offset = page * size;

      //System.out.println("--서비스--");
      //System.out.println(page + " " +  size + " " + status + " " + target + " " + job + " " + sort + " " + order);

      List<PlanListDTO> plans = listMapper.getAllPlanList(mode, offset, size, status, target, job, sort, order);
      int totalPlans = listMapper.getTotalPlanCount(mode, status, target, job);

      Map<String, Object> response = new HashMap<>();
      response.put("plans", plans);
      response.put("currentPage", page + 1);
      response.put("totalPages", (int) Math.ceil((double) totalPlans / size));
      response.put("totalPosts", totalPlans);

      return response;
    } catch (Exception e) {
      log.error("e: ", e);
      // 예외 발생 시 반환할 응답
      Map<String, Object> errorResponse = new HashMap<>();
      errorResponse.put("error", "An error occurred while retrieving the plan list.");
      errorResponse.put("message", e.getMessage()); // 예외 메시지 추가
      errorResponse.put("status", "error"); // 에러 상태 추가
      return errorResponse; // 실패 응답 반환
    }
  }
}
