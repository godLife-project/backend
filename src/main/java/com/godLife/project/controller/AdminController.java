package com.godLife.project.controller;

import com.godLife.project.service.ChallengeService;
import com.godLife.project.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
  private UserService userService;
  private ChallengeService challengeService;

  public AdminController(UserService userService, ChallengeService challengeService) {
    this.userService = userService;
    this.challengeService = challengeService;
  }

}
