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

  @Override
  public List<NoticeDTO> getNoticeList(){
    return noticeMapper.getNoticeList();
  }

  // 상세 조회
  public NoticeDTO getNoticeDetail(int noticeIdx){
    return noticeMapper.getNoticeDetail(noticeIdx);
  }

  // 공지 작성
  public int createNotice(NoticeDTO noticeDTO){
    return noticeMapper.createNotice(noticeDTO);
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
