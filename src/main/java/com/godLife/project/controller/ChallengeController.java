package com.godLife.project.controller;

import com.godLife.project.dto.contents.ChallengeDTO;
import com.godLife.project.dto.jwtDTO.CustomUserDetails;
import com.godLife.project.dto.request.ChallengeJoinRequest;
import com.godLife.project.dto.verify.ChallengeVerifyDTO;
import com.godLife.project.dto.verify.VerifyRecordDTO;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.jwt.LoginFilter;
import com.godLife.project.service.interfaces.ChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/challenges")
public class ChallengeController {

  @Autowired
  private GlobalExceptionHandler handler;

  @Autowired
  private final ChallengeService challengeService;

  public ChallengeController(ChallengeService challengeService) {
    this.challengeService = challengeService;
  }


  @Operation(summary = "최신 챌린지 조회 API", description = "최신순으로 조회하며 종료된 챌린지는 조회하지 않음")
  @GetMapping("/latest")
  public ResponseEntity<Map<String, Object>> getLatestChallenges(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int size) {

    List<ChallengeDTO> challenges = challengeService.getLatestChallenges(page, size);
    int totalChallenges = challengeService.getTotalLatestChallenges();
    int totalPages = (int) Math.ceil((double) totalChallenges / size);

    if (challenges.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT)
              .body(createResponse(204, "최신 챌린지가 없습니다."));
    }

    Map<String, Object> response = createResponse(200, "최신 챌린지 조회 성공");
    response.put("challenges", challenges);
    response.put("totalPages", totalPages);
    response.put("currentPage", page);
    response.put("pageSize", size);

    return ResponseEntity.ok(response);
  }

  @Operation(summary = "카테고리별 챌린지 조회 API", description = "선택한 카테고리의 챌린지를 최신순으로 조회")
  @GetMapping("/latest/{challCategoryIdx}")
  public ResponseEntity<Map<String, Object>> getChallengesByCategoryId(
          @PathVariable int challCategoryIdx,
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int size) {

    List<ChallengeDTO> challenges = challengeService.getChallengesByCategoryId(challCategoryIdx, page, size);
    int totalChallenges = challengeService.getTotalChallengesByCategory(challCategoryIdx);
    int totalPages = (int) Math.ceil((double) totalChallenges / size);

    Map<String, Object> response;

    if (challenges.isEmpty()) {
      response = createResponse(200, "해당 카테고리에 챌린지가 없습니다.");
      response.put("challenges", new ArrayList<>()); // 빈 리스트
      response.put("totalPages", 0);
      response.put("currentPage", page);
      response.put("pageSize", size);
      return ResponseEntity.ok(response);
    }

    response = createResponse(200, "카테고리별 챌린지 조회 성공");
    response.put("challenges", challenges);
    response.put("totalPages", totalPages);
    response.put("currentPage", page);
    response.put("pageSize", size);

    return ResponseEntity.ok(response);
  }

  // 응답 생성 메서드
  private Map<String, Object> createResponse(int status, String message) {
    Map<String, Object> response = new HashMap<>();
    response.put("status", status);
    response.put("message", message);
    return response;
  }

  // 챌린지 상세 조회
  @GetMapping("/{challIdx}")
  public ResponseEntity<Map<String, Object>> getChallengeDetail(
          @PathVariable Long challIdx) {
    Map<String, Object> response = new HashMap<>();

    try {
      ChallengeDTO challengeDetail = challengeService.getChallengeDetail(challIdx);
      response.put("success", true);
      response.put("challenge", challengeDetail);
      return ResponseEntity.ok(response);

    } catch (IllegalArgumentException e) {
      response.put("success", false);
      response.put("message", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
  }

  // 인증 기록 조회
  @GetMapping("/verify-records/{challIdx}")
  public ResponseEntity<Map<String, Object>> getVerifyRecords(
          @PathVariable Long challIdx,
          @RequestHeader(value = "Authorization", required = false) String authHeader) {

    Long userIdx = null;

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      try {
        userIdx = (long) handler.getUserIdxFromToken(authHeader);
      } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "success", false,
                "message", "Invalid token"
        ));
      }
    }

    if (userIdx == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
              "success", false,
              "message", "로그인이 필요합니다."
      ));
    }

    List<VerifyRecordDTO> records = challengeService.getVerifyRecords(challIdx, userIdx);

    return ResponseEntity.ok(Map.of(
            "success", true,
            "records", records
    ));
  }


  // 챌린지 참여
  @PostMapping("/auth/join/{challIdx}")
  public ResponseEntity<Object> joinChallenge(@PathVariable Long challIdx,
                                              @RequestHeader("Authorization") String authHeader,
                                              @RequestBody ChallengeJoinRequest joinRequest) {
    try {
      int userIdx = handler.getUserIdxFromToken(authHeader);
      ChallengeDTO challenge = challengeService.joinChallenge(
              challIdx,
              userIdx,
              joinRequest.getActivity(),
              joinRequest.getActivityTime()
      );
      return ResponseEntity.ok(challenge);

    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(handler.createResponse(400, e.getMessage()));
    } catch (Exception e) {
      log.error("e: ", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(handler.createResponse(500, "챌린지 참여 중 오류가 발생했습니다."));
    }
  }


    // 챌린지 인증 (경과 시간 기록)
    @PostMapping("/auth/verify/{challIdx}")
    public ResponseEntity<Map<String, Object>> verifyChallenge(
            @PathVariable Long challIdx,
            @RequestBody ChallengeVerifyDTO dto,
            @RequestHeader("Authorization") String authHeader) {

      Map<String, Object> response = new LinkedHashMap<>();
      try {
        int userIdx = handler.getUserIdxFromToken(authHeader);

        dto.setChallIdx(challIdx);
        dto.setUserIdx((long) userIdx); // userIdx 세팅 누락 방지

        challengeService.verifyChallenge(dto);

        response.put("status", 200);
        response.put("message", "인증이 완료되었습니다.");
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);

      } catch (IllegalArgumentException | IllegalStateException e) {
        response.put("status", 400);
        response.put("error", "Bad Request");
        response.put("message", e.getMessage());
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.badRequest().body(response);

      } catch (Exception e) {
        response.put("status", 500);
        response.put("error", "Internal Server Error");
        response.put("message", "서버 오류가 발생했습니다.");
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
      }
    }


  // 챌린지 검색 API (제목, 카테고리)
  @GetMapping("/search")
  public List<ChallengeDTO> searchChallenges(
          @RequestParam(required = false) String challTitle,
          @RequestParam(required = false) Integer challCategoryIdx,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size,
          @RequestParam(defaultValue = "chall_idx") String sort
  ) {
    int offset = page * size;
    return challengeService.searchChallenges(challTitle, challCategoryIdx, offset, size, sort);
  }
}
