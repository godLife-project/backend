package com.godLife.project.service;

import com.godLife.project.dto.datas.PlanDTO;

import java.util.List;

public interface PlanService {
    void writePlan(PlanDTO plan);

    // 계획리스트 (랭킹순)
    List<PlanDTO> getRankingPlans();
    // 계획 리스트 (최신순)
    List<PlanDTO> getLatestPlans();
}
