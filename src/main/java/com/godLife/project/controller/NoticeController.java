package com.godLife.project.controller;

import com.godLife.project.dto.contents.NoticeDTO;
import com.godLife.project.service.interfaces.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notice")
@RequiredArgsConstructor
public class NoticeController {
  private NoticeService noticeService;

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
  public ResponseEntity<NoticeDTO> createNotice(@RequestBody NoticeDTO noticeDTO) {
    // 작성 시 작성일 설정
    noticeDTO.setNoticeDate(LocalDateTime.now());
    noticeService.createNotice(noticeDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(noticeDTO);
  }

  // 공지 수정
  @PutMapping("/admin/{noticeIdx}")
  public ResponseEntity<?> modifyNotice(@PathVariable int noticeIdx,
                                        @RequestBody NoticeDTO noticeDTO) {
  noticeDTO.setNoticeIdx(noticeIdx);
  noticeService.modifyNotice(noticeDTO);
  return ResponseEntity.ok().build();
  }

  // 공지 삭제
  @DeleteMapping("/admin/{noticeIdx}")
  public ResponseEntity<?> deleteNotice(@PathVariable int noticeIdx) {
    noticeService.deleteNotice(noticeIdx);
    return  ResponseEntity.ok().build();
  }

}
