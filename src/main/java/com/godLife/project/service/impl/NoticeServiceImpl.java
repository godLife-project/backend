package com.godLife.project.service.impl;

import com.godLife.project.dto.contents.NoticeDTO;
import com.godLife.project.mapper.NoticeMapper;
import com.godLife.project.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
  private NoticeMapper noticeMapper;

  @Override
  public List<NoticeDTO> getNoticeList(){
    return noticeMapper.getNoticeList();
  }
  // 상세 조회
  public NoticeDTO getNoticeDetail(int noticeIdx){
    return noticeMapper.getNoticeDetail(noticeIdx);
  }

  // 공지 수정
  public void modifyNotice(NoticeDTO noticeDTO){
    int  updateCount = noticeMapper.modifyNotice(noticeDTO);
    // 1건만 수정 되는지 확인
    if (updateCount != 1) {
      throw new IllegalArgumentException("챌린지 수정 실패");
    }
  }

  // 공지 삭제
  public void deleteNotice(int noticeIdx){
    int deleteCount =  noticeMapper.deleteNotice(noticeIdx);
    if (deleteCount != 1) {
      throw new IllegalArgumentException("챌린지 삭제 실패");
    }
  }
 }
