package com.godLife.project.service.impl;

import com.godLife.project.dto.contents.NoticeDTO;
import com.godLife.project.mapper.NoticeMapper;
import com.godLife.project.service.interfaces.NoticeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {
  private NoticeMapper noticeMapper;

  public NoticeServiceImpl(NoticeMapper noticeMapper) {
    this.noticeMapper = noticeMapper;
  }

  // 공지사항 전체 조회
  @Override
  public List<NoticeDTO> getNoticeList(int page, int size) {
    int offset = (page - 1) * size;
    return noticeMapper.getNoticeList(offset, size);
  }
  public int totalNoticeCount() {
    return noticeMapper.totalNoticeCount();
  }

  // 상세 조회
  public NoticeDTO getNoticeDetail(int noticeIdx){
    return noticeMapper.getNoticeDetail(noticeIdx);
  }

  // 팝업 공지 조회
  public List<NoticeDTO> getActivePopupNoticeList() {
    return noticeMapper.getActivePopupNoticeList();
  }

  // 기존 공지 팝업 활성화
  public int setNoticePopup(NoticeDTO noticeDTO) {
    int result = noticeMapper.setNoticePopup(noticeDTO);
    return result == 1 ? 200 : 404; // 업데이트 성공 → 200, 실패(공지 없음) → 404
  }

  // 공지 작성
  public int createNotice(NoticeDTO noticeDTO) {
    int result = noticeMapper.createNotice(noticeDTO);
    return result == 1 ? 201 : 500; // 1이면 성공, 그 외는 서버 에러 처리
  }

  // 공지 수정
  public int modifyNotice(NoticeDTO noticeDTO){
    int  updateCount = noticeMapper.modifyNotice(noticeDTO);
    // 1건만 수정 되는지 확인
    if (updateCount != 1) {
      throw new IllegalArgumentException("챌린지 수정 실패");
    }
    return 200;
  }

  // 공지 삭제 서비스 메소드
  public int deleteNotice(int noticeIdx) {
    // 공지 존재 여부 확인
    int deleteCount = noticeMapper.deleteNotice(noticeIdx);
    if (deleteCount != 1) {
      return 404; // 공지가 존재하지 않는 경우
    }

    return 200; // 공지 삭제 완료
  }
 }
