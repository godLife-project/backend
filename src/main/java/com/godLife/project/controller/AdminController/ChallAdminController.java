package com.godLife.project.controller.AdminController;

import com.godLife.project.dto.contents.ChallengeDTO;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.service.interfaces.AdminInterface.ChallAdminService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/admin/challenges")
public class ChallAdminController {
  @Autowired
  private GlobalExceptionHandler handler;

  @Autowired
  private final ChallAdminService challAdminService;

  public ChallAdminController(ChallAdminService challAdminService){this.challAdminService = challAdminService;}

  // 챌린지 생성 API
  @PostMapping("/create")
  public ResponseEntity<Map<String, Object>> createChallenge(@Valid @RequestBody ChallengeDTO challengeDTO,
                                                             BindingResult result) {
    if (result.hasErrors()) {
      return ResponseEntity.badRequest().body(handler.getValidationErrors(result));
    }

    try {
      int insertResult = challAdminService.createChallenge(challengeDTO);

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

  // ----------- 챌린지 수정 --------------
  @PatchMapping("/modify")
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
    if (!challAdminService.existsById(challengeDTO.getChallIdx())) {
      log.warn("챌린지 존재하지 않음: challIdx={}", challengeDTO.getChallIdx());
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(handler.createResponse(404, "요청하신 챌린지가 존재하지 않습니다."));
    }

    try {
      // 수정 로직 실행
      int modifyResult = challAdminService.modifyChallenge(challengeDTO);
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

  //              ------------- 삭제 --------------
  @PatchMapping("/delete")
  public ResponseEntity<Map<String, Object>> deleteChallenge(
          @Valid @RequestBody ChallengeDTO challengeDTO,
          BindingResult result)
  {
    // 유효성 검사 실패 시 에러 반환
    if (result.hasErrors()) {
      return ResponseEntity.badRequest().body(handler.getValidationErrors(result));
    }

    // 챌린지 존재 여부 확인
    Long challIdx = challengeDTO.getChallIdx();
    if (!challAdminService.existsById(challIdx)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(handler.createResponse(404, "요청하신 챌린지가 존재하지 않습니다."));
    }

    // 삭제 서비스 실행
    int deleteResult = challAdminService.deleteChallenge(challIdx); // 관리자 권한 체크 후 삭제

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


  //  -----------  챌린지 조기 종료  --------------
  @PutMapping("/earlyFinish/{challIdx}")
  public ResponseEntity<String> earlyFinish(@PathVariable Long challIdx) {
    try {
      challAdminService.earlyFinishChallenge(challIdx);
      return ResponseEntity.ok("챌린지가 성공적으로 종료되었습니다.");
    } catch (IllegalStateException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  //  -------------- 챌린지 검색 API (제목, 카테고리) -------------
  @GetMapping("/search")
  public List<ChallengeDTO> searchChallenges(
          @RequestParam(required = false) String challTitle,
          @RequestParam(required = false) String challCategory,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size,
          @RequestParam(defaultValue = "chall_idx DESC") String sort
  ) {
    int offset = page * size;
    return challAdminService.searchChallenges(challTitle, challCategory, offset, size, sort);
  }


    //             ------------- 리스트 조회 -----------------
  @GetMapping("/latest")
  public ResponseEntity<Map<String, Object>> getAllChallengesAdmin(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int size) {

    List<ChallengeDTO> challenges = challAdminService.getAllChallengesAdmin(page, size);
    int totalChallenges = challAdminService.getTotalLatestChallenges();
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

  // ------------- 카테고리 조회 -----------------
  @GetMapping("/latest/{challCategoryIdx}")
  public ResponseEntity<Map<String, Object>> getChallengesByCategoryId(
          @PathVariable int challCategoryIdx,
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int size) {

    List<ChallengeDTO> challenges = challAdminService.getChallengesByCategoryId(challCategoryIdx, page, size);
    int totalChallenges = challAdminService.getTotalChallengesByCategory(challCategoryIdx);
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

  // -------------  챌린지 상세 조회  -----------------
  @GetMapping("/detail/{challIdx}")
  public ChallengeDTO getChallengeDetail(@PathVariable Long challIdx) {
    // 서비스에서 챌린지 상세 정보 조회 및 업데이트
    return challAdminService.getChallengeDetail(challIdx);
  }
}
