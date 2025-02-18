package com.godLife.project.controller;

import com.godLife.project.dto.datas.UserDTO;
import com.godLife.project.exception.LoginFailedException;
import com.godLife.project.service.TestService;
import com.godLife.project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "로그인", description = "로그인 ID,Pw API")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        System.out.println("받은데이터 :" + userDTO.getUserId() + ","+ userDTO.getUserPw());
        try {
            UserDTO user = userService.login(userDTO.getUserId(), userDTO.getUserPw());
            return ResponseEntity.ok(user); // 로그인 성공 시 사용자 정보 반환
        } catch (LoginFailedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: 아이디 또는 비밀번호가 잘못되었습니다.");
        }
    }
}