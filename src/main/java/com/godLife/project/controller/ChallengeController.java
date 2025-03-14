package com.godLife.project.controller;

import com.godLife.project.dto.contents.ChallengeDTO;
import com.godLife.project.dto.infos.VerifyDTO;
import com.godLife.project.exception.UnauthorizedException;
import com.godLife.project.service.interfaces.ChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

  public ChallengeController(ChallengeService challengeService) {
    this.challengeService = challengeService;
  }


    // 챌린지 생성 API
    @PostMapping("/admin/create")
    public ResponseEntity<String> createChallenge(@RequestBody ChallengeDTO challengeDTO) {
        try {
            challengeService.createChallenge(challengeDTO);
            return ResponseEntity.ok("챌린지가 성공적으로 생성되었습니다.");
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/auth/{challIdx}/start")
    public ResponseEntity<String> updateChallengeStartTime(@PathVariable Long challIdx, @RequestParam Integer duration) {
        try {
            challengeService.updateChallengeStartTime(challIdx, duration);
            return ResponseEntity.ok("챌린지 시작 시간이 업데이트되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("챌린지 시작 시간 업데이트 실패: " + e.getMessage());
        }
    }


    @Operation(summary = "챌린지 조회 API", description = "최신순으로 조회 및 종료된 챌린지 조회 x")
    // 최신 챌린지 가져오기 API
    @GetMapping("/latest")
    public ResponseEntity<List<ChallengeDTO>> getLatestChallenges() {
        List<ChallengeDTO> challenges = challengeService.getLatestChallenges();
        return ResponseEntity.ok(challenges);
    }

    // 챌린지 상세 조회
    @GetMapping("/detail")
    public ChallengeDTO getChallengeDetail(@RequestParam Long challIdx,
                                           @RequestParam int userJoin,
                                           @RequestParam Integer duration) {
        // ChallengeService를 호출하여 챌린지 상세정보 조회
        return challengeService.getChallnegeDetail(challIdx, userJoin, duration);
    }

    // 카테고리 챌린지 조회 api
    @GetMapping("/latest/{challCategoryIdx}")
    public ResponseEntity<List<ChallengeDTO>> getChallengesByCategoryId(@PathVariable int challCategoryIdx){
        try {
            // 서비스에서 카테고리별 챌린지 조회
            List<ChallengeDTO> challenges = challengeService.getChallengesByCategoryId(challCategoryIdx);

            // 결과 반환
            if (challenges.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(challenges); // 비어있을 경우
            }
            return ResponseEntity.ok(challenges); // 결과가 있으면 200 OK 반환
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 챌린지 참여 API
    @PostMapping("/auth/join/{challIdx}")
    public ResponseEntity<ChallengeDTO> joinChallenge(@RequestParam Long challIdx,
                                                      @RequestParam int userIdx,
                                                      @RequestParam Integer duration,
                                                      @RequestParam LocalDateTime startTime,
                                                      @RequestParam LocalDateTime endTime,
                                                      @RequestParam String activity) {
        try {
            ChallengeDTO challenge = challengeService.joinChallenge(challIdx, userIdx, duration, startTime, endTime, activity);
            return ResponseEntity.ok(challenge); // 참여 후 챌린지 상세 정보 반환
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // 에러 처리
        }
    }

    // 챌린지 인증 (경과 시간 기록)
    @PostMapping("/auth/verify/{challIdx}")
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
    @PutMapping("/admin/{challIdx}")
    public ResponseEntity<?> modifyChallenge(
            @PathVariable Long challIdx,
            @RequestBody ChallengeDTO challengeDTO
    ) {
        challengeDTO.setChallIdx(challIdx);  // 경로에서 받은 ID를 DTO에 넣음
        challengeService.modifyChallenge(challengeDTO);
        return ResponseEntity.ok().build();
    }

    // 챌린지 삭제
    @DeleteMapping("/admin/{challIdx}")
    public ResponseEntity<?> deleteChallenge(@PathVariable Long challIdx){
        challengeService.deleteChallenge(challIdx);
        return ResponseEntity.ok().build();
    }

}
