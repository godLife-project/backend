package com.godLife.project.controller;

import com.godLife.project.dto.contents.ChallengeDTO;
import com.godLife.project.service.ChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenges")
public class ChallengeController {

    private ChallengeService challengeService;

    @Autowired
    public void ChallengeService(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @Operation(summary = "챌린지 조회 API", description = "최신순으로 조회 및 종료된 챌린지 조회 x")
    // 최신 챌린지 가져오기 API
    @GetMapping("/latest")
    public ResponseEntity<List<ChallengeDTO>> getLatestChallenges() {
        List<ChallengeDTO> challenges = challengeService.getLatestChallenges();
        return ResponseEntity.ok(challenges);
    }

    //  챌린지 참여 API (일반 사용자용)
    @PostMapping("/join")
    public ResponseEntity<String> joinChallenge(@RequestParam Long userId, @RequestParam int challIdx) {
        try {
            challengeService.joinChallenge(userId, challIdx);
            return ResponseEntity.ok("챌린지 참여 성공!");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("챌린지 참여 중 오류가 발생했습니다.");
        }
    }
}
