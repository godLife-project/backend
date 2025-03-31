package com.godLife.project.service.interfaces;

import com.godLife.project.dto.contents.QnADTO;
import com.godLife.project.dto.infos.SearchQueryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QnaService {
  List<QnADTO> selectAllQna();
  QnADTO getQnaById(int qnaIdx);
  int createQna(QnADTO qna);
  int updateQna(QnADTO qna);
  int deleteQna(@Param("qnaIdx") int qnaIdx, int userIdx);

  List<QnADTO> searchQna(SearchQueryDTO searchQuery);

  // 답변 저장
  void saveAnswer(int qnaIdx, int aIdx, String aSub);

}
