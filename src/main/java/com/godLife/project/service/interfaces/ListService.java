package com.godLife.project.service.interfaces;

import com.godLife.project.dto.list.MyPlanDTO;

import java.util.List;
import java.util.Map;

public interface ListService {

  // 나의 진행중인 루틴 리스트 조회
  List<MyPlanDTO> getMyPlansList(int userIdx);

  // 모든 루틴 리스트 조회
  Map<String, Object> getAllPlansList(String mode, int page, int size, int status, List<Integer> target,
                                      List<Integer> job, String sort, String order, String search, int userIdx);

  // 좋아요 한 루틴 리스트 조회
  Map<String, Object> getLikePlanList(String mode, int page, int size, int status, List<Integer> target,
                                      List<Integer> job, String order, String search, int userIdx);

}
