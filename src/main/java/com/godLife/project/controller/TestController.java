package com.godLife.project.controller;

import com.godLife.project.dto.datas.UserDTO;
import com.godLife.project.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @Operation(summary = "직업 카테고리 직업명 조회", description = "직업 카테고리의 이름을 조회하는 API")
    @GetMapping("/getJob")
    public List<String> getJobName() {
        return testService.getJobName();
    }

    @Operation(summary = "유저 테이블 조회", description = "유저 테이블의 모든 정보를 조회하는 API")
    @GetMapping("/getAllUser")
    public List<UserDTO> getAllUsers() {
        return testService.getAllUsers();
    }

    @Operation(summary = "유저 테이블 조회", description = "유저 테이블에서 인덱스 번호에 맞는 유저를 조회하는 API")
    @GetMapping("/{userIdx}")
    public UserDTO getUserById(@PathVariable int userIdx) {
        return  testService.getUserById(userIdx);
    }
}
