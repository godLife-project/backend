package com.godLife.project.mapper;

import com.godLife.project.dto.contents.QnADTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QnaMapper {
  QnADTO selectQnaById(@Param("qnaIdx") int qnaIdx);
  List<QnADTO> selectAllQna();
  void insertQna(QnADTO qna);
  void updateQna(QnADTO qna);
  void deleteQna(@Param("qnaIdx") int qnaIdx);
  List<QnADTO> searchQna(@Param("query") String query);
}
