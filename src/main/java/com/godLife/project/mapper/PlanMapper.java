package com.godLife.project.mapper;

import com.godLife.project.dto.datas.PlanDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PlanMapper {
    // 루틴 작성 메서드
    void insertPlan(PlanDTO plan);

    // 최신 계획 리스트
    List<PlanDTO> getLatestPlans();

    // 가중치를 적용한 랭킹 순으로 계획 조회
    List<PlanDTO> getRankingPlans();
}