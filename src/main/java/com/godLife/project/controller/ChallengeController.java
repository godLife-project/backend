package com.godLife.project.controller;

import com.godLife.project.dto.contents.ChallengeDTO;
import com.godLife.project.dto.infos.VerifyDTO;
import com.godLife.project.dto.request.ChallengeJoinRequest;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.service.interfaces.ChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
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


  // 챌린지 생성 API
  @PostMapping("/admin/create")
  public ResponseEntity<Map<String, Object>> createChallenge(@Valid @RequestBody ChallengeDTO challengeDTO,
                                                             BindingResult result) {
    if (result.hasErrors()) {
      return ResponseEntity.badRequest().body(handler.getValidationErrors(result));
    }

    try {
      int insertResult = challengeService.createChallenge(challengeDTO);

      // 응답 메세지 세팅
      String msg = switch (insertResult) {
        case 201 -> "챌린지 작성 완료";
        case 500 -> "서버 내부적으로 오류가 발생하여 챌린지를 저장하지 못했습니다.";
        default -> "알 수 없는 오류가 발생했습니다.";
      };

      return ResponseEntity.status(handler.getHttpStatus(insertResult)).body(handler.createResponse(insertResult, msg));

    } catch (IllegalArgumentException e) {
      log.error("잘못된 요청: {}", e.getMessage());
      return ResponseEntity.badRequest().body(handler.createResponse(400, e.getMessage()));
    } catch (Exception e) {
      log.error("서버 오류 발생: ", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(handler.createResponse(500, "예기치 못한 오류가 발생했습니다."));
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

    if (challenges.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT)
              .body(createResponse(204, "해당 카테고리에 챌린지가 없습니다."));
    }

    Map<String, Object> response = createResponse(200, "카테고리별 챌린지 조회 성공");
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


  @PostMapping("/auth/join/{challIdx}")
  public ResponseEntity<Object> joinChallenge(@PathVariable Long challIdx,
                                              @RequestBody ChallengeJoinRequest joinRequest) {
    try {
      // 챌린지 참여 로직
      ChallengeDTO challenge = challengeService.joinChallenge(challIdx, joinRequest.getUserIdx(), joinRequest);
      return ResponseEntity.ok(challenge); // 참가한 챌린지 정보 반환
    } catch (IllegalArgumentException e) {
      // 참가 인원 초과 등의 문제 발생 시 400 응답
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(handler.createResponse(400, e.getMessage()));
    } catch (Exception e) {
      // 기타 예외는 500 응답
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(handler.createResponse(500, "챌린지 참여 중 오류가 발생했습니다."));
    }
  }

    // 챌린지 인증 (경과 시간 기록)
    @PostMapping("/auth/verify/{challIdx}")
    public ResponseEntity<?> verifyChallenge(
            @PathVariable Long challIdx,
            @RequestParam int userIdx,
            @RequestBody VerifyDTO verifyDTO) {

      verifyDTO.setChallIdx(challIdx);
      verifyDTO.setUserIdx(userIdx);

      // 인증 처리 (챌린지 정보는 서비스 내부에서 조회)
      challengeService.verifyChallenge(verifyDTO);

      return ResponseEntity.ok("챌린지 인증 완료");
    }


    // 챌린지 수정

    @PatchMapping("/admin/modify")
    public ResponseEntity<Map<String, Object>> modifyChallenge(
            @Valid @RequestBody ChallengeDTO challengeDTO,
            BindingResult result
    ) {
      log.info(" 챌린지 수정 요청: {}", challengeDTO); // 요청 정보 로깅

      // 유효성 검사 실패 시 에러 반환
      if (result.hasErrors()) {
        log.warn("유효성 검사 실패: {}", result.getFieldErrors());
        return ResponseEntity.badRequest().body(handler.getValidationErrors(result));
      }

      // 챌린지 존재 여부 확인
      if (!challengeService.existsById(challengeDTO.getChallIdx())) {
        log.warn("챌린지 존재하지 않음: challIdx={}", challengeDTO.getChallIdx());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(handler.createResponse(404, "요청하신 챌린지가 존재하지 않습니다."));
      }

      try {
        // 수정 로직 실행
        int modifyResult = challengeService.modifyChallenge(challengeDTO);
        log.info("챌린지 수정 결과: challIdx={}, modifyResult={}", challengeDTO.getChallIdx(), modifyResult);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", modifyResult);

        switch (modifyResult) {
          case 1, 200 -> { // 1과 200을 같은 처리로 묶음
            response.put("message", "챌린지 수정 완료");
            return ResponseEntity.ok(response);
          }
          case 403 -> {
            log.warn("수정 권한 없음: challIdx={}", challengeDTO.getChallIdx());
            response.put("message", "작성자가 아닙니다. 재로그인 해주세요.");
          }
          case 404 -> {
            log.warn("수정 대상 챌린지 없음: challIdx={}", challengeDTO.getChallIdx());
            response.put("message", "요청하신 챌린지가 존재하지 않습니다.");
          }
          case 500 -> {
            log.error("서버 내부 오류 발생: challIdx={}", challengeDTO.getChallIdx());
            response.put("message", "서버 내부적으로 오류가 발생하여 챌린지를 수정하지 못했습니다.");
          }
          default -> {
            log.error("예상치 못한 오류 발생: challIdx={}, modifyResult={}", challengeDTO.getChallIdx(), modifyResult);
            response.put("message", "알 수 없는 오류가 발생했습니다. 관리자에게 문의하세요.");
          }
        }

        return ResponseEntity.status(handler.getHttpStatus(modifyResult)).body(response);
      } catch (Exception e) {
        log.error("예외 발생: challIdx={}, error={}", challengeDTO.getChallIdx(), e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("status", 500, "message", "서버 내부 오류 발생"));
      }
    }


  @PatchMapping("/admin/delete")
  public ResponseEntity<Map<String, Object>> deleteChallenge(
          @Valid @RequestBody ChallengeDTO challengeDTO,
          BindingResult result)
  {
    // 유효성 검사 실패 시 에러 반환
    if (result.hasErrors()) {
      return ResponseEntity.badRequest().body(handler.getValidationErrors(result));
    }

    // 챌린지 존재 여부 확인
    if (!challengeService.existsById(challengeDTO.getChallIdx())) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(handler.createResponse(404, "요청하신 챌린지가 존재하지 않습니다."));
    }

    // 삭제 서비스 실행
    int deleteResult = challengeService.deleteChallenge(challengeDTO); // 관리자 권한 체크 후 삭제

    // 응답 메시지 설정
    String msg = switch (deleteResult) {
      case 200 -> "챌린지 삭제 완료";
      case 403 -> "관리자 권한이 없습니다.";
      case 404 -> "요청하신 챌린지가 존재하지 않습니다.";
      case 500 -> "서버 내부 오류로 챌린지를 삭제하지 못했습니다.";
      default -> "알 수 없는 오류가 발생했습니다.";
    };

    return ResponseEntity.status(handler.getHttpStatus(deleteResult))
            .body(handler.createResponse(deleteResult, msg));
  }

}
