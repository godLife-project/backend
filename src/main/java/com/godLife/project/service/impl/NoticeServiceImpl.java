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
 }
