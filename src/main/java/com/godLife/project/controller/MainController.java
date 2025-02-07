package com.godLife.project.controller;

import com.godLife.project.dto.MainDataDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/main")
public class MainController {

    @Operation(summary = "메인 데이터 조회", description = "메인 페이지 데이터를 조회하는 API")
    @GetMapping("/data")
    public ResponseEntity<MainDataDTO> getMainData() {
        MainDataDTO data = new MainDataDTO();
        data.setIdx(1L);
        data.setMessage("Hello, Swagger!");
        return ResponseEntity.ok(data);
    }
}
