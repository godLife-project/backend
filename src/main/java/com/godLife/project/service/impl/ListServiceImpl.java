package com.godLife.project.service.impl;

import com.godLife.project.dto.list.MyPlanDTO;
import com.godLife.project.dto.list.customDTOs.CustomPlanDTO;
import com.godLife.project.mapper.ListMapper;
import com.godLife.project.mapper.PlanMapper;
import com.godLife.project.service.interfaces.ListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
}
