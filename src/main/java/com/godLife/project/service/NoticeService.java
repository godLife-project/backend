package com.godLife.project.service;

import com.godLife.project.dto.contents.NoticeDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoticeService {
  List<NoticeDTO> getNoticeList();
  // 공지 상세조회
  NoticeDTO getNoticeDetail(int noticeIdx);
  // 공지 수정
  void modifyNotice(NoticeDTO noticeDTO);
  //공지 삭제
  void deleteNotice(int noticeIdx);
}
