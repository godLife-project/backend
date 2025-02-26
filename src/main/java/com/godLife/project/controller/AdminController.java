package com.godLife.project.controller;

import com.godLife.project.dto.contents.ChallengeDTO;
import com.godLife.project.exception.UnauthorizedException;
import com.godLife.project.service.ChallengeService;
import com.godLife.project.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController {
  private UserService userService;
  private ChallengeService challengeService;

  public AdminController(UserService userService, ChallengeService challengeService) {
    this.userService = userService;
    this.challengeService = challengeService;
  }

  @GetMapping("/admin")
  public String adminP() {
    return "Admin Controller";
  }


  // 챌린지 생성 API (관리자 전용)
  @PostMapping("/create")
  public ResponseEntity<String> createChallenge(@RequestBody ChallengeDTO challengeDTO) {
    try {
      challengeService.createChallenge(challengeDTO);
      return ResponseEntity.ok("챌린지가 성공적으로 생성되었습니다.");
    } catch (UnauthorizedException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("챌린지 생성 중 오류가 발생했습니다.");
    }
  }
}
