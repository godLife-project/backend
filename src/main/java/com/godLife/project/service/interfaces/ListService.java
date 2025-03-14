package com.godLife.project.service.interfaces;

import com.godLife.project.dto.list.MyPlanDTO;

import java.util.List;

public interface ListService {

  List<MyPlanDTO> getMyPlansList(int userIdx);
}
