package com.godLife.project.controller;

import com.godLife.project.dto.contents.ChallengeDTO;
import com.godLife.project.dto.infos.VerifyDTO;
import com.godLife.project.service.ChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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


    // 챌린지 참여 (시작)
    @PostMapping("/{challIdx}/join")
    public ResponseEntity<?> joinChallenge(
            @PathVariable Long challIdx,
            @RequestParam int userIdx,
            @RequestParam @NotNull LocalDateTime challEndTime) { // 종료시간 필수
        try {
            challengeService.joinChallenge(challIdx, userIdx, challEndTime);
            return ResponseEntity.ok("챌린지 참여 완료");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("종료 시간을 지정해야 합니다.");
        }
    }

    // 챌린지 인증 (경과 시간 기록)
    @PostMapping("/{challIdx}/verify")
    public ResponseEntity<?> verifyChallenge(
            @PathVariable Long challIdx,
            @RequestParam int userIdx,
            @RequestBody VerifyDTO verifyDTO,
            @RequestBody ChallengeDTO challengeDTO) {
        verifyDTO.setChallIdx((Long) challIdx);
        verifyDTO.setUserIdx(userIdx);
        challengeService.verifyChallenge(verifyDTO, challengeDTO);
        return ResponseEntity.ok("챌린지 인증 완료");
    }

    // 챌린지 수정
    @PutMapping("/{challIdx}")
    public ResponseEntity<?> modifyChallenge(
            @PathVariable Long challIdx,
            @RequestBody ChallengeDTO challengeDTO
    ) {
        challengeDTO.setChallIdx(challIdx);  // 경로에서 받은 ID를 DTO에 넣음
        challengeService.modifyChallenge(challengeDTO);
        return ResponseEntity.ok().build();
    }

    // 챌린지 삭제
    @DeleteMapping("/{challIdx}")
    public ResponseEntity<?> deleteChallenge(@PathVariable Long challIdx){
        challengeService.deleteChallenge(challIdx);
        return ResponseEntity.ok().build();
    }

}
