package com.godLife.project.service.impl;

import com.godLife.project.dto.datas.PlanDTO;
import com.godLife.project.mapper.PlanMapper;
import com.godLife.project.service.PlanService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanServicelmpl implements PlanService {

    private final PlanMapper planMapper;

    public PlanServicelmpl(PlanMapper planMapper) { this.planMapper = planMapper; }

    @Override
    public void writePlan(PlanDTO plan){
        // 입력값 검증 (필요에 따라 추가)
            if (plan == null) {
            throw new IllegalArgumentException("Plan object cannot be null.");
        }

            // 예시: 루틴 시작일과 종료일이 올바른지 체크
            if (plan.getPlanSubStart() == null || plan.getPlanSubEnd() == null) {
            throw new IllegalArgumentException("Plan start date and end date must be provided.");
        }

        // 추가 비즈니스 로직이 필요한 경우 여기서 처리 가능

        // Mapper를 사용하여 DB에 계획 정보 등록
        planMapper.insertPlan(plan);
    }

    // 가중치를 적용한 랭킹 순으로 계획 조회
    public List<PlanDTO> getRankingPlans() {
        return planMapper.getRankingPlans();
    }

    // 계획 리스트페이지 (최신순)
    public List<PlanDTO> getLatestPlans() {
        return planMapper.getLatestPlans();
    }
}
