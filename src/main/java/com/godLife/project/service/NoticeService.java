package com.godLife.project.service;

import com.godLife.project.dto.contents.NoticeDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoticeService {
  List<NoticeDTO> getNoticeList();
  NoticeDTO getNoticeDetail(int noticeIdx);
}
