package com.godLife.project.controller;


import com.godLife.project.dto.datas.UserDTO;
import com.godLife.project.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/user", produces = "application/json; charset=UTF-8")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }



    //@GetMapping("/login")


    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<Map<String, String>> join (@Valid @RequestBody UserDTO joinUserDTO, BindingResult result) {
        // 유효성 검사
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(fieldError -> {
                 errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            });

            return ResponseEntity.badRequest().body(errors);
        }
        else {
            // 데이터 삽입
            String successMessage = userService.insertUser(joinUserDTO);
            // 삽입 완료시 완료 메세지
            if ("Success".equals(successMessage)) {
                Map<String, String> success = new HashMap<>();
                success.put("message", "회원가입 완료");
                return ResponseEntity.ok(success);
            }
            // 삽입 에러시 에러 메세지
            else {
                Map<String, String> error = new HashMap<>();
                error.put("message", "회원가입 실패");
                return ResponseEntity.badRequest().body(error);
            }
        }
    }

    // 아이디 중복 체크
    @GetMapping("/checkId/{userId}")
    public ResponseEntity<Boolean> checkUserIdExist(@PathVariable String userId) {
        Boolean isAvailable = userService.checkUserIdExist(userId);
        return ResponseEntity.ok(isAvailable);
    }



}
