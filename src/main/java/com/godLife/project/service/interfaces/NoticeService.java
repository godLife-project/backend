package com.godLife.project.service.interfaces;

import com.godLife.project.dto.contents.NoticeDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoticeService {
  List<NoticeDTO> getNoticeList(int page, int size);
  // 공지 상세조회
  NoticeDTO getNoticeDetail(int noticeIdx);
  // 팝업 공지사항 조회
  NoticeDTO getActivePopupNotice();
  // 기존 공지 팝업 활성화
  int setNoticePopup(NoticeDTO noticeDTO);
  // 공지 작성
  int createNotice(NoticeDTO noticeDTO);
  // 공지 수정
  int modifyNotice(NoticeDTO noticeDTO);
  //공지 삭제
  int deleteNotice(int noticeIdx);
}
