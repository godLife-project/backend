package com.godLife.project.service.interfaces;

import com.godLife.project.dto.contents.QnaDTO;
import com.godLife.project.dto.qnaWebsocket.QnaMatchedListDTO;
import com.godLife.project.dto.qnaWebsocket.WaitListMessageDTO;

import java.util.List;

public interface QnaService {

  // 1:1 문의 작성
  void createQna(QnaDTO qnaDTO);

  // 매칭된 문의 리스트 조회 (개인)
  List<QnaMatchedListDTO> getlistAllMatchedQna(int adminIdx);

  // 대기중인 문의 리스트 조회 (전체)
  WaitListMessageDTO getlistAllWaitQna(String chatRoomNo);

  // 문의 본문 조회
  String getQnaContent(int qnaIdx);
}
