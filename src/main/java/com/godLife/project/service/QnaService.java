package com.godLife.project.service;

import com.godLife.project.dto.contents.QnADTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QnaService {
  List<QnADTO> selectAllQna();
  QnADTO getQnaById(int qnaIdx);
  int createQna(QnADTO qna);
  int updateQna(QnADTO qna);
  int deleteQna(@Param("qnaIdx") int qnaIdx, int userIdx);
  List<QnADTO> searchQna(@Param("query") String query);

  // 답변 저장
  void saveAnswer(int qnaIdx, int aIdx, String aSub);

}
