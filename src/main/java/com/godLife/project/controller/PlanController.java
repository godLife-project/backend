package com.godLife.project.controller;

import com.godLife.project.dto.datas.PlanDTO;
import com.godLife.project.service.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plan")
public class PlanController {
    private final PlanService planService;

    @Autowired
    public PlanController(PlanService planService) {this.planService = planService;}

    // 계획작성
    @Operation(summary = "루틴 작성", description = "루틴 작성 로직")
    @PostMapping("/write") // POST /plans/write
    public ResponseEntity<String> writePlan(@RequestBody PlanDTO plan) {
        try {
            planService.writePlan(plan);
            return ResponseEntity.ok("Plan created successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // 랭킹 순서대로 계획 반환
    @GetMapping("/ranking")
    public ResponseEntity<List<PlanDTO>> getRankingPlans() {
        List<PlanDTO> plans = planService.getRankingPlans();
        return ResponseEntity.ok(plans);
    }
    // 최신 순서대로 계획 반환
    @GetMapping("/latest")
    public ResponseEntity<List<PlanDTO>> getLatestPlans() {
        List<PlanDTO> plans = planService.getLatestPlans();
        return ResponseEntity.ok(plans);
    }
}
