package com.godLife.project.controller;

import com.godLife.project.dto.datas.PlanDTO;
import com.godLife.project.service.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
