package com.godLife.project.mapper;

import com.godLife.project.dto.contents.NoticeDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {
  // 공지사항 리스트 조회
  List<NoticeDTO> getNoticeList();

  // 공지 상세 조회
  NoticeDTO getNoticeDetail(int noticeIdx);
  // 공지 작성
  int createNotice(NoticeDTO noticeDTO);
  // 수정
  int modifyNotice(NoticeDTO noticeDTO);
  // 삭제
  int deleteNotice(int noticeIdx);

}
