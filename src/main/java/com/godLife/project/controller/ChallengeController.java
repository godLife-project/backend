package com.godLife.project.controller;

import com.godLife.project.dto.contents.ChallengeDTO;
import com.godLife.project.dto.infos.VerifyDTO;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.service.interfaces.ChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/challenges")
public class ChallengeController {

  @Autowired
  private GlobalExceptionHandler handler;


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
      int insertResult = challengeService.createChallenge(challengeDTO);

      // 응답 메세지 세팅
      String msg = "";
      switch (insertResult) {
        case 201 -> msg = "챌린지 작성 완료";
        case 500 -> msg = "서버 내부적으로 오류가 발생하여 챌린지를 저장하지 못했습니다.";
        default -> msg = "알 수 없는 오류가 발생했습니다.";
      }

      // 응답 메시지 설정
      return ResponseEntity.status(handler.getHttpStatus(insertResult)).body(handler.createResponse(insertResult, msg));
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


  @GetMapping("/latest/{challCategoryIdx}")
  public ResponseEntity<Map<String, Object>> getChallengesByCategoryId(@PathVariable int challCategoryIdx) {
    // 카테고리별 챌린지 조회
    List<ChallengeDTO> challenges = challengeService.getChallengesByCategoryId(challCategoryIdx);

    // 조회된 챌린지가 없을 경우
    if (challenges.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT)
              .body(handler.createResponse(204, "해당 카테고리에 챌린지가 없습니다."));
    }

    // 카테고리별 챌린지가 있을 경우
    Map<String, Object> response = handler.createResponse(200, "카테고리별 챌린지 조회 성공");
    response.put("challenges", challenges);  // 챌린지 리스트를 응답에 추가
    return ResponseEntity.ok(response);
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
    @PatchMapping("/admin/modify")
    public ResponseEntity<Map<String, Object>> modifyChallenge(
            @Valid @RequestBody ChallengeDTO challengeDTO,
            BindingResult result
    ) {
      // 유효성 검사 실패 시 에러 반환
      if (result.hasErrors()) {
        return ResponseEntity.badRequest().body(handler.getValidationErrors(result));
      }

      // 챌린지 존재 여부 확인
      if (!challengeService.existsById(challengeDTO.getChallIdx())) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(handler.createResponse(404, "요청하신 챌린지가 존재하지 않습니다."));
      }

      // 수정 로직 실행
      int modifyResult = challengeService.modifyChallenge(challengeDTO);

      // 응답 메시지 세팅
      String msg = switch (modifyResult) {
        case 200 -> "챌린지 수정 완료";
        case 403 -> "작성자가 아닙니다. 재로그인 해주세요.";
        case 404 -> "요청하신 챌린지가 존재하지 않습니다.";
        case 500 -> "서버 내부적으로 오류가 발생하여 챌린지를 수정하지 못했습니다.";
        default -> "알 수 없는 오류가 발생했습니다.";
      };

      return ResponseEntity.status(handler.getHttpStatus(modifyResult))
              .body(handler.createResponse(modifyResult, msg));
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
