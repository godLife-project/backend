package com.godLife.project.controller;

import com.godLife.project.dto.contents.NoticeDTO;
import com.godLife.project.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notice")
@RequiredArgsConstructor
public class NoticeController {
  private NoticeService  noticeService;

  @GetMapping
  public ResponseEntity<List<NoticeDTO>> getNoticeList() {
    List<NoticeDTO> notices =  noticeService.getNoticeList();
    return ResponseEntity.ok(notices);
  }

  // 공지 상세 조회
  @GetMapping("/{noticeIdx}")
  public  ResponseEntity<NoticeDTO> getNoticeDetail(@PathVariable int noticeIdx) {
    NoticeDTO notice =  noticeService.getNoticeDetail(noticeIdx);
    if (notice == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // 공지 없음 404 오류 처리
    }
    return ResponseEntity.ok(notice);
  }
}
