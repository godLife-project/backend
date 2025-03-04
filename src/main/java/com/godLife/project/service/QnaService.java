package com.godLife.project.service;

import com.godLife.project.dto.contents.QnADTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QnaService {
  List<QnADTO> selectAllQna();
  QnADTO getQnaById(int qnaIdx);
  void createQna(QnADTO qna);
  void updateQna(QnADTO qna);
  void deleteQna(@Param("qnaIdx") int qnaIdx);
  List<QnADTO> searchQna(@Param("query") String query);
}
