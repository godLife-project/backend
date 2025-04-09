package com.godLife.project.mapper;

import com.godLife.project.dto.contents.NoticeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeMapper {
  // 공지사항 리스트 조회
  List<NoticeDTO> getNoticeList(@Param("offset") int offset, @Param("size") int size);

  // 공지 상세 조회
  NoticeDTO getNoticeDetail(int noticeIdx);
  // 활성화된 팝업 공지사항 조회
  NoticeDTO getActivePopupNotice();
  // 기존 공지 팝업 활성화
  int setNoticePopup(NoticeDTO noticeDTO);
  // 공지 작성
  int createNotice(NoticeDTO noticeDTO);
  // 수정
  int modifyNotice(NoticeDTO noticeDTO);
  // 삭제
  int deleteNotice(int noticeIdx);

}
