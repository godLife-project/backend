package com.godLife.project.controller;

import com.godLife.project.dto.contents.NoticeDTO;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.service.interfaces.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notice")
@RequiredArgsConstructor
public class NoticeController {
  @Autowired
  private final GlobalExceptionHandler handler;

  private final NoticeService noticeService;

  @GetMapping
  public ResponseEntity<List<NoticeDTO>> getNoticeList() {
    List<NoticeDTO> notices = noticeService.getNoticeList();
    return ResponseEntity.ok(notices);
  }

  // 공지 상세 조회
  @GetMapping("/{noticeIdx}")
  public ResponseEntity<NoticeDTO> getNoticeDetail(@PathVariable int noticeIdx) {
    NoticeDTO notice = noticeService.getNoticeDetail(noticeIdx);
    if (notice == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // 공지 없음 404 오류 처리
    }
    return ResponseEntity.ok(notice);
  }

  // 공지 작성
  @PostMapping("/admin/create")
  public ResponseEntity<Map<String, Object>> createNotice(@RequestHeader("Authorization") String authHeader,
                                                          @Valid @RequestBody NoticeDTO noticeDTO,
                                                          BindingResult result) {

    // 유효성 검사 오류 처리
    if (result.hasErrors()) {
      return ResponseEntity.badRequest().body(handler.getValidationErrors(result));
    }

    // 작성일 설정
    noticeDTO.setNoticeDate(LocalDateTime.now());

    // userIdx 조회 (관리자 여부 확인)
    int userIdx = handler.getUsernameFromToken(authHeader);
    noticeDTO.setUserIdx(userIdx);

    // 공지 작성
    int insertResult = noticeService.createNotice(noticeDTO);

    // 응답 메시지 세팅
    String msg = switch (insertResult) {
      case 201 -> "공지 작성 완료";
      case 403 -> "권한이 없습니다. 관리자 계정으로 로그인해주세요.";
      case 404 -> "요청하신 공지가 존재하지 않습니다.";
      case 409 -> "중복된 공지가 존재합니다.";
      case 500 -> "서버 내부 오류로 인해 공지를 작성하지 못했습니다.";
      default -> "알 수 없는 오류가 발생했습니다.";
    };

    // 응답 반환
    return ResponseEntity.status(handler.getHttpStatus(insertResult))
            .body(handler.createResponse(insertResult, msg));
  }

  // 공지 수정 API
  @PatchMapping("/admin/{noticeIdx}")
  public ResponseEntity<Map<String, Object>> modifyNotice(@PathVariable int noticeIdx,
                                                          @RequestHeader("Authorization") String authHeader,
                                                          @Valid @RequestBody NoticeDTO noticeDTO, BindingResult result) {
    // 유효성 검사 실패 시 에러 반환
    if (result.hasErrors()) {
      return ResponseEntity.badRequest().body(handler.getValidationErrors(result));
    }

    // 관리자 인증 여부 확인
    int userIdx = handler.getUsernameFromToken(authHeader);
    noticeDTO.setUserIdx(userIdx);

    // 공지 존재 여부 확인
    int modifyResult = noticeService.modifyNotice(noticeDTO);

    // 응답 메시지 세팅
    String msg = "";
    switch (modifyResult) {
      case 200 -> msg = "공지 수정 완료";
      case 403 -> msg = "관리자가 아닙니다. 재로그인 해주세요.";
      case 404 -> msg = "요청하신 공지가 존재하지 않습니다.";
      case 500 -> msg = "서버 내부적으로 오류가 발생하여 공지를 수정하지 못했습니다.";
      default -> msg = "알 수 없는 오류가 발생했습니다.";
    }

    // 응답 메시지 설정
    return ResponseEntity.status(handler.getHttpStatus(modifyResult))
            .body(handler.createResponse(modifyResult, msg));
  }

  // 공지 삭제 API
  @DeleteMapping("/admin/{noticeIdx}")
  public ResponseEntity<Map<String, Object>> deleteNotice(@PathVariable int noticeIdx) {
    // 공지 삭제 처리
    int deleteResult = noticeService.deleteNotice(noticeIdx);

    // 응답 메시지 세팅
    String msg = "";
    switch (deleteResult) {
      case 200 -> msg = "공지 삭제 완료";
      case 404 -> msg = "요청하신 공지가 존재하지 않습니다.";
      case 500 -> msg = "서버 내부적으로 오류가 발생하여 공지를 삭제하지 못했습니다.";
      default -> msg = "알 수 없는 오류가 발생했습니다.";
    }

    // 응답 메시지 설정
    return ResponseEntity.status(handler.getHttpStatus(deleteResult))
            .body(handler.createResponse(deleteResult, msg));
  }

}
