package com.godLife.project.controller;

import com.godLife.project.dto.contents.NoticeDTO;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.service.interfaces.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/api/notice")
@RequiredArgsConstructor
public class NoticeController {
  @Autowired
  private final GlobalExceptionHandler handler;

  private final NoticeService noticeService;


  // 공지 목록 조회
  @GetMapping
  public ResponseEntity<Map<String, Object>> getNoticeList(@RequestParam(defaultValue = "1") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
    try {
      List<NoticeDTO> notices = noticeService.getNoticeList(page, size);
      int totalNotices = noticeService.totalNoticeCount(); // 전체 개수 구해서 페이지 계산
      int totalPages = (int) Math.ceil((double) totalNotices / size);

      Map<String, Object> response = new HashMap<>();
      response.put("status", 200);
      response.put("message", "공지사항 조회 성공");
      response.put("data", notices);
      response.put("totalPages", totalPages);
      response.put("currentPage", page);
      response.put("pageSize", size);

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      String msg = "서버 오류로 인해 공지사항을 불러올 수 없습니다.";
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(Map.of("status", 500, "message", msg));
    }
  }

  // 공지 상세 조회
  @GetMapping("/{noticeIdx}")
  public ResponseEntity<Map<String, Object>> getNoticeDetail(@PathVariable int noticeIdx) {
    try {
      NoticeDTO notice = noticeService.getNoticeDetail(noticeIdx);

      if (notice == null) {
        throw new NoSuchElementException("조회하려는 공지가 존재하지 않습니다.");
      }

      return ResponseEntity.ok().body(handler.createResponse(200, notice));

    } catch (NoSuchElementException e) {
      String msg = "공지 조회 실패, 조회하려는 공지가 존재하지 않습니다.";
      System.out.println(e);
      return ResponseEntity.status(handler.getHttpStatus(404)).body(handler.createResponse(404, msg));

    } catch (Exception e) {
      String msg = "서버 내부 오류로 인해 공지 조회에 실패했습니다.";
      System.out.println(e);
      return ResponseEntity.status(handler.getHttpStatus(500)).body(handler.createResponse(500, msg));
    }
  }

  @GetMapping("/popup")
  public ResponseEntity<Map<String, Object>> getPopupNotice() {
    List<NoticeDTO> popupNotice = noticeService.getActivePopupNoticeList();

    if (popupNotice != null) {
      Map<String, Object> response = handler.createResponse(200, "팝업 공지사항 조회 성공");
      response.put("notice", popupNotice);
      return ResponseEntity.ok(response);
    } else {
      return ResponseEntity.status(HttpStatus.NO_CONTENT)
              .body(handler.createResponse(204, "현재 표시할 팝업 공지사항이 없습니다."));
    }
  }

  @PatchMapping("/admin/popup")
  public ResponseEntity<Map<String, Object>> setNoticePopup(@RequestHeader("Authorization") String authHeader,
                                                            @RequestBody NoticeDTO noticeDTO) {
    try {

      int result = noticeService.setNoticePopup(noticeDTO);

      String msg = switch (result) {
        case 200 -> "팝업 설정이 성공적으로 업데이트되었습니다.";
        case 404 -> "해당 공지를 찾을 수 없습니다.";
        case 500 -> "서버 내부 오류로 인해 팝업 설정을 변경할 수 없습니다.";
        default -> "알 수 없는 오류가 발생했습니다.";
      };

      return ResponseEntity.status(handler.getHttpStatus(result))
              .body(handler.createResponse(result, msg));

    } catch (Exception e) {
      log.error("팝업 설정 중 예외 발생: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(handler.createResponse(400, "요청 데이터가 잘못되었거나 서버 처리 중 오류가 발생했습니다."));
    }
  }


  // 공지 작성 API
  @PostMapping("/admin/create")
  public ResponseEntity<Map<String, Object>> createNotice(@RequestHeader("Authorization") String authHeader,
                                                          @RequestBody NoticeDTO noticeDTO,
                                                          BindingResult result) {
    if (result.hasErrors()) {
      log.error("Validation Error: {}", result.getAllErrors());
      return ResponseEntity.badRequest().body(handler.getValidationErrors(result));
    }

    int userIdx = handler.getUserIdxFromToken(authHeader);
    noticeDTO.setUserIdx(userIdx);
    noticeDTO.setNoticeDate(LocalDateTime.now());

    // 서비스 호출
    int statusCode = noticeService.createNotice(noticeDTO);

    // 메시지 매핑
    String msg = switch (statusCode) {
      case 201 -> "공지 작성 완료";
      case 403 -> "권한이 없습니다.";
      case 409 -> "중복된 공지입니다.";
      case 500 -> "서버 오류입니다.";
      default -> "알 수 없는 오류입니다.";
    };

    return ResponseEntity.status(handler.getHttpStatus(statusCode))
            .body(handler.createResponse(statusCode, msg));
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

    noticeDTO.setNoticeIdx(noticeIdx);  // 수정할 공지의 IDX 설정

    // 공지 수정
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
