package com.godLife.project.controller;

import com.godLife.project.dto.contents.ChallengeDTO;
import com.godLife.project.dto.request.ChallengeJoinRequest;
import com.godLife.project.dto.verify.ChallengeVerifyDTO;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.service.interfaces.ChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  @GetMapping("/detail/{challIdx}")
  public ChallengeDTO getChallengeDetail(@PathVariable Long challIdx) {
    // 서비스에서 챌린지 상세 정보 조회 및 업데이트
    return challengeService.getChallengeDetail(challIdx);
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
    public ResponseEntity<String> verifyChallenge(
            @PathVariable Long challIdx,
            @RequestBody ChallengeVerifyDTO dto,
            @RequestHeader("Authorization") String authHeader) {

      int userIdx = handler.getUserIdxFromToken(authHeader);

      dto.setChallIdx(challIdx);
      dto.setUserIdx((long) userIdx); // userIdx 세팅 누락 방지

      challengeService.verifyChallenge(dto);

      return ResponseEntity.ok("인증이 완료되었습니다.");
    }


  // 챌린지 검색 API (제목, 카테고리)
  @GetMapping("/search")
  public List<ChallengeDTO> searchChallenges(
          @RequestParam(required = false) String challTitle,
          @RequestParam(required = false) String challCategory,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size,
          @RequestParam(defaultValue = "chall_idx DESC") String sort
  ) {
    int offset = page * size;
    return challengeService.searchChallenges(challTitle, challCategory, offset, size, sort);
  }
}
