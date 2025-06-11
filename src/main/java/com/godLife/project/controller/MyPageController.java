package com.godLife.project.controller;

import com.godLife.project.dto.list.PlanListDTO;
import com.godLife.project.dto.request.myPage.*;
import com.godLife.project.dto.response.MyPageUserInfosResponseDTO;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.service.impl.redis.RedisService;
import com.godLife.project.service.interfaces.ListService;
import com.godLife.project.service.interfaces.MyPageService;
import com.godLife.project.service.interfaces.jwtInterface.RefreshService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/myPage/auth")
@RequiredArgsConstructor
public class MyPageController {

  private final GlobalExceptionHandler handler;

  private final MyPageService myPageService;

  private final RefreshService refreshService;

  private final RedisService redisService;

  private final ListService listService;


  // 유저 정보 추가 제공
  @GetMapping("/myAccount")
  public ResponseEntity<Map<String, Object>> getMyInfos(@RequestHeader("Authorization") String authHeader) {

    int userIdx = handler.getUserIdxFromToken(authHeader);

    MyPageUserInfosResponseDTO result = myPageService.getUserInfos(userIdx);
    System.out.println("result: " + result);

    if (result == null) {
      return ResponseEntity.status(404).body(handler.createResponse(404, "유저 정보가 없습니다."));
    }
    // 응답 메시지 설정
    return ResponseEntity.status(handler.getHttpStatus(200)).body(handler.createResponse(200, result));
  }

  // 개인 정보 수정
  @PatchMapping("/myAccount/modify/personal")
  public ResponseEntity<Map<String, Object>> modifyMyInfos(@RequestHeader("Authorization") String authHeader,
                                                           @Valid @RequestBody ModifyPersonalRequestDTO modifyPersonalRequestDTO,
                                                           BindingResult valid) {
    if (valid.hasErrors()) {
      return ResponseEntity.badRequest().body(handler.getValidationErrors(valid));
    }

    int userIdx = handler.getUserIdxFromToken(authHeader);
    modifyPersonalRequestDTO.setUserIdx(userIdx);

    int result = myPageService.modifyPersonal(modifyPersonalRequestDTO);

    // 응답 메세지 세팅
    String msg = "";
    switch (result) {
      case 200 -> msg = "개인정보 수정 완료";
      case 404 -> msg = "탈퇴했거나, 존재하지 않는 유저입니다.";
      case 500 -> msg = "서버 내부적으로 오류가 발생하여 요청을 수행하지 못했습니다.";
      default -> msg = "알 수 없는 오류가 발생했습니다.";
    }

    // 응답 메시지 설정
    return ResponseEntity.status(handler.getHttpStatus(result)).body(handler.createResponse(result, msg));
  }

  // 닉네임 수정
  @PatchMapping("/myAccount/modify/nickName")
  public ResponseEntity<Map<String, Object>> modifyNickName(@RequestHeader("Authorization") String authHeader,
                                                            @Valid @RequestBody ModifyNicknameRequestDTO modifyNicknameRequestDTO,
                                                            BindingResult valid) {
    if (valid.hasErrors()) {
      return ResponseEntity.badRequest().body(handler.getValidationErrors(valid));
    }

    int userIdx = handler.getUserIdxFromToken(authHeader);
    modifyNicknameRequestDTO.setUserIdx(userIdx);

    int result = myPageService.modifyNickName(modifyNicknameRequestDTO);

    // 응답 메세지 세팅
    String msg = "";
    switch (result) {
      case 200 -> msg = "닉네임 수정 완료";
      case 404 -> msg = "탈퇴했거나, 존재하지 않는 유저입니다.";
      case 500 -> msg = "서버 내부적으로 오류가 발생하여 요청을 수행하지 못했습니다.";
      default -> msg = "알 수 없는 오류가 발생했습니다.";
    }

    // 응답 메시지 설정
    return ResponseEntity.status(handler.getHttpStatus(result)).body(handler.createResponse(result, msg));
  }

  // 이메일 수정
  @PatchMapping("/myAccount/modify/email")
  public ResponseEntity<Map<String, Object>> modifyEmail(@RequestHeader("Authorization") String authHeader,
                                                         @Valid @RequestBody ModifyEmailRequestDTO modifyEmailRequestDTO,
                                                         BindingResult valid) {
    // 이메일 유효성 검증
    if (valid.hasErrors()) {
      return ResponseEntity.badRequest().body(handler.getValidationErrors(valid));
    }
    // 이메일 인증 여부 검증
    String verified = redisService.getStringData("EMAIL_VERIFIED: " + modifyEmailRequestDTO.getUserEmail());
    if (verified == null || !verified.equals("true")) {
      return ResponseEntity.status(handler.getHttpStatus(412))
          .body(handler.createResponse(412, "이메일 인증이 필요합니다."));
    }

    int userIdx = handler.getUserIdxFromToken(authHeader);
    modifyEmailRequestDTO.setUserIdx(userIdx);

    int result = myPageService.modifyEmail(modifyEmailRequestDTO);

    redisService.deleteData("EMAIL_VERIFIED: " + modifyEmailRequestDTO.getUserEmail());

    // 응답 메세지 세팅
    String msg = "";
    switch (result) {
      case 200 -> msg = "이메일 수정 완료";
      case 404 -> msg = "탈퇴했거나, 존재하지 않는 유저입니다.";
      case 500 -> msg = "서버 내부적으로 오류가 발생하여 요청을 수행하지 못했습니다.";
      default -> msg = "알 수 없는 오류가 발생했습니다.";
    }

    // 응답 메시지 설정
    return ResponseEntity.status(handler.getHttpStatus(result)).body(handler.createResponse(result, msg));
  }

  // 직업/목표 수정
  @PatchMapping("/myAccount/modify/job-target")
  public ResponseEntity<Map<String, Object>> modifyJobAndTarget(@RequestHeader("Authorization") String authHeader,
                                                                @Valid @RequestBody ModifyJobTargetRequestDTO jobTargetRequestDTO,
                                                                BindingResult valid) {
    if (valid.hasErrors()) {
      return ResponseEntity.badRequest().body(handler.getValidationErrors(valid));
    }
    int userIdx = handler.getUserIdxFromToken(authHeader);
    jobTargetRequestDTO.setUserIdx(userIdx);

    int result = myPageService.modifyJobTarget(jobTargetRequestDTO);

    // 응답 메세지 세팅
    String msg = "";
    switch (result) {
      case 200 -> msg = "직업 / 목표 수정 완료";
      case 404 -> msg = "탈퇴했거나, 존재하지 않는 유저입니다.";
      case 500 -> msg = "서버 내부적으로 오류가 발생하여 요청을 수행하지 못했습니다.";
      default -> msg = "알 수 없는 오류가 발생했습니다.";
    }

    // 응답 메시지 설정
    return ResponseEntity.status(handler.getHttpStatus(result)).body(handler.createResponse(result, msg));
  }

  // 비밀번호 변경
  @PatchMapping("/security/change/password")
  public ResponseEntity<Map<String, Object>> modifyPassword(@RequestHeader("Authorization") String authHeader,
                                                            @Valid @RequestBody GetUserPwRequestDTO userPwRequestDTO,
                                                            BindingResult valid) {
    if (valid.hasErrors()) {
      return ResponseEntity.badRequest().body(handler.getValidationErrors(valid));
    }

    int userIdx = handler.getUserIdxFromToken(authHeader);
    userPwRequestDTO.setUserIdx(userIdx);

    int result = myPageService.modifyPassword(userPwRequestDTO);

    // 응답 메세지 세팅
    String msg = "";
    switch (result) {
      case 200 -> msg = "비밀번호 수정 완료";
      case 400 -> msg = "비밀번호 확인 필드의 값이 누락되었습니다.";
      case 403 -> msg = "현재 비밀번호가 틀렸습니다.";
      case 404 -> msg = "탈퇴했거나, 존재하지 않는 유저입니다.";
      case 409 -> msg = "현재 비밀번호와 동일한 비밀번호로 수정할 수 없습니다.";
      case 422 -> msg = "비밀번호가 일치하지 않습니다.";
      case 500 -> msg = "서버 내부적으로 오류가 발생하여 요청을 수행하지 못했습니다.";
      default -> msg = "알 수 없는 오류가 발생했습니다.";
    }

    // 응답 메시지 설정
    return ResponseEntity.status(handler.getHttpStatus(result)).body(handler.createResponse(result, msg));
  }


  // 회원 탈퇴
  @PatchMapping("/accountDeletion")
  public ResponseEntity<Map<String, Object>> accountDeletion(@RequestBody GetUserPwRequestDTO getUserPwRequestDTO,
                                                             @RequestHeader("Authorization") String authHeader,
                                                             HttpServletRequest request, HttpServletResponse response) {
    int userIdx = handler.getUserIdxFromToken(authHeader);
    String userPw = getUserPwRequestDTO.getUserPw();

    int result = myPageService.deleteAccount(userIdx, userPw);

    // 응답 메세지 세팅
    String msg = "";
    switch (result) {
      case 200 -> msg = "회원 탈퇴 완료";
      case 403 -> msg = "비밀번호가 틀렸습니다.";
      case 404 -> msg = "이미 탈퇴했거나 없는 계정입니다.";
      case 500 -> msg = "서버 내부적으로 오류가 발생하여 요청을 수행하지 못했습니다.";
      default -> msg = "알 수 없는 오류가 발생했습니다.";
    }

    String refresh = getRefreshTokenFromCookies(request);

    //로그아웃 진행
    //Refresh 토큰 DB에서 제거
    refreshService.deleteByRefresh(refresh);

    //Refresh 토큰 Cookie 값 0
    Cookie cookie = new Cookie("refresh", null);
    cookie.setMaxAge(0);
    cookie.setPath("/");
    response.addCookie(cookie);

    // 응답 메시지 설정
    return ResponseEntity.status(handler.getHttpStatus(result)).body(handler.createResponse(result, msg));
  }

  // 회원 탈퇴 취소
  @PatchMapping("/accountDeletion/cancel")
  public ResponseEntity<Map<String, Object>> accountDeletionCancel(@RequestBody GetUserPwRequestDTO getUserPwRequestDTO,
                                                                   @RequestHeader("Authorization") String authHeader) {
    int userIdx = handler.getUserIdxFromToken(authHeader);
    String userPw = getUserPwRequestDTO.getUserPw();

    int result = myPageService.deleteCancelAccount(userIdx, userPw);

    // 응답 메세지 세팅
    String msg = "";
    switch (result) {
      case 200 -> msg = "탈퇴 취소 완료";
      case 403 -> msg = "비밀번호가 틀렸습니다.";
      case 404 -> msg = "이미 완전히 삭제 되었거나 아직 탈퇴하지 않은 계정입니다.";
      case 500 -> msg = "서버 내부적으로 오류가 발생하여 요청을 수행하지 못했습니다.";
      default -> msg = "알 수 없는 오류가 발생했습니다.";
    }

    // 응답 메시지 설정
    return ResponseEntity.status(handler.getHttpStatus(result)).body(handler.createResponse(result, msg));
  }

  // 나의 루틴 기록 조회
  @GetMapping("/list/myPlan")
  public ResponseEntity<Map<String, Object>> getMyPlanList(@RequestParam(defaultValue = "1") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "0") int status,
                                                           @RequestParam(required = false) List<Integer> target,
                                                           @RequestParam(required = false) List<Integer> job,
                                                           @RequestParam(defaultValue = "latest") String sort,
                                                           @RequestParam(defaultValue = "desc") String order,
                                                           @RequestParam(required = false) String search,
                                                           @RequestHeader("Authorization") String authHeader) {

    //System.out.println("--컨트롤러--");
    //System.out.println(page + " " +  size + " " + status + " " + target + " " + job + " " + sort + " " + order);
    String mode = "private";
    int userIdx = handler.getUserIdxFromToken(authHeader);
    Map<String, Object> response = listService.getAllPlansList(mode, page - 1, size, status, target, job, sort, order, search, userIdx);

    Object plans = response.get("plans");

    if (plans instanceof List<?>) {
      List<PlanListDTO> tempList = ((List<?>) plans).stream()
          .filter(PlanListDTO.class::isInstance)  // PlanListDTO 타입만 필터링
          .map(PlanListDTO.class::cast)
          .toList();
      if (tempList.isEmpty()) {
        return ResponseEntity.status(handler.getHttpStatus(204)).build();
      }
    }

    return ResponseEntity.ok(response);
  }

  // 선택 루틴 일괄 삭제
  @PatchMapping("/delete/plans")
  public ResponseEntity<Map<String, Object>> deleteMyPlans(@RequestHeader("Authorization") String authHeader,
                                                           @RequestBody List<Integer> planIndexes) {
    // userIdx 조회
    int userIdx = handler.getUserIdxFromToken(authHeader);

    // 서비스 로직 실행
    int result = myPageService.deleteSelectPlans(userIdx, planIndexes);

    // 응답 메세지 세팅
    String msg = "";
    switch (result) {
      case 200 -> msg = "루틴 일괄 삭제 완료";
      case 404 -> msg = "루틴이 존재하지 않거나, 작성자가 아닙니다.";
      case 500 -> msg = "서버 내부적으로 오류가 발생하여 요청을 수행하지 못했습니다.";
      default -> msg = "알 수 없는 오류가 발생했습니다.";
    }

    // 응답 메시지 설정
    return ResponseEntity.status(handler.getHttpStatus(result))
        .body(handler.createResponse(result, msg));
  }

  // 선택 루틴 일괄 공개/비공개 전환
  @PatchMapping("/switch/isShared")
  public ResponseEntity<Map<String, Object>> switchMyPlansIsShared(@RequestHeader("Authorization") String authHeader,
                                                                   @RequestBody List<Integer> planIndexes,
                                                                   @RequestParam(defaultValue = "reverse") String mode) {
    // userIdx 조회
    int userIdx = handler.getUserIdxFromToken(authHeader);

    // 서비스 로직 실행
    int result = myPageService.switchIsSharedBySelectPlans(userIdx, planIndexes, mode);

    // 응답 메세지 세팅
    String msg = "";
    switch (result) {
      case 200 -> msg = "공개 여부 일괄 전환 완료";
      case 404 -> msg = "루틴이 존재하지 않거나, 작성자가 아닙니다.";
      case 500 -> msg = "서버 내부적으로 오류가 발생하여 요청을 수행하지 못했습니다.";
      default -> msg = "알 수 없는 오류가 발생했습니다.";
    }

    // 응답 메시지 설정
    return ResponseEntity.status(handler.getHttpStatus(result))
        .body(handler.createResponse(result, msg));
  }

  // 내가 참여한 챌린지 조회
  @GetMapping("/list/myChall")
  public ResponseEntity<Map<String, Object>> getMyChallList(@RequestHeader("Authorization") String authHeader) {

    int result = 200;

    // 응답 메세지 세팅
    String msg = "";
    switch (result) {
      case 200 -> msg = "공개 여부 일괄 전환 완료";
      case 404 -> msg = "루틴이 존재하지 않거나, 작성자가 아닙니다.";
      case 500 -> msg = "서버 내부적으로 오류가 발생하여 요청을 수행하지 못했습니다.";
      default -> msg = "알 수 없는 오류가 발생했습니다.";
    }

    // 응답 메시지 설정
    return ResponseEntity.status(handler.getHttpStatus(result))
        .body(handler.createResponse(result, msg));
  }

  // 내가 좋아요 한 루틴 조회
  @GetMapping("/list/myLike")
  public ResponseEntity<Map<String, Object>> getMyLikeList(@RequestParam(defaultValue = "1") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(required = false) List<Integer> target,
                                                           @RequestParam(required = false) List<Integer> job,
                                                           @RequestParam(defaultValue = "desc") String order,
                                                           @RequestParam(required = false) String search,
                                                           @RequestHeader("Authorization") String authHeader) {
    int userIdx = handler.getUserIdxFromToken(authHeader);
    String mode = "myLike";
    int status = 0;

    Map<String, Object> response = listService.getLikePlanList(mode, page - 1, size, status, target, job, order, search, userIdx);

    Object plans = response.get("plans");

    if (plans instanceof List<?>) {
      List<PlanListDTO> tempList = ((List<?>) plans).stream()
          .filter(PlanListDTO.class::isInstance)  // PlanListDTO 타입만 필터링
          .map(PlanListDTO.class::cast)
          .toList();
      if (tempList.isEmpty()) {
        return ResponseEntity.status(handler.getHttpStatus(204)).build();
      }
    }

    return ResponseEntity.ok(response);
  }

  // 선택 루틴 일괄 삭제
  @DeleteMapping("/delete/likes")
  public ResponseEntity<Map<String, Object>> deleteMyLikes(@RequestHeader("Authorization") String authHeader,
                                                           @RequestParam List<Integer> planIndexes) {
    // userIdx 조회
    int userIdx = handler.getUserIdxFromToken(authHeader);

    //System.out.println(planIndexes);

    // 서비스 로직 실행
    int result = myPageService.unLikeSelectPlans(userIdx, planIndexes);

    // 응답 메세지 세팅
    String msg = "";
    switch (result) {
      case 200 -> msg = "좋아요 일괄 취소 완료";
      case 404 -> msg = "좋아요 취소할 루틴이 없습니다.";
      case 500 -> msg = "서버 내부적으로 오류가 발생하여 요청을 수행하지 못했습니다.";
      default -> msg = "알 수 없는 오류가 발생했습니다.";
    }

    // 응답 메시지 설정
    return ResponseEntity.status(handler.getHttpStatus(result))
        .body(handler.createResponse(result, msg));
  }







  /* -----------------------------------------// 함수 구현 //------------------------------------------------------- */
  private String getRefreshTokenFromCookies(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null)  {
      System.out.println("쿠키 없음");
      return null;
    }

    for (Cookie cookie : cookies) {
      //System.out.println(cookie.getName());
      if ("refresh".equals(cookie.getName())) {
        return cookie.getValue();
      }
    }
    return null;
  }
  /* --------------------------------------------------------------------------------------------------------------- */


}
