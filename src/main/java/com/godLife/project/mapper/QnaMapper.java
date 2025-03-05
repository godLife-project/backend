package com.godLife.project.mapper;

import com.godLife.project.dto.contents.QnADTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QnaMapper {
  QnADTO selectQnaById(@Param("qnaIdx") int qnaIdx);
  List<QnADTO> selectAllQna();
  int insertQna(QnADTO qna);
  int updateQna(QnADTO qna);
  List<QnADTO> searchQna(@Param("query") String query);

  @Delete("DELETE FROM QNA_TABLE WHERE QNA_IDX = #{qnaIdx}")
  int deleteQna(@Param("qnaIdx") int qnaIdx);

  // 답변
  void updateAnswer(@Param("qnaIdx") int qnaIdx,
                    @Param("aIdx") int aIdx,
                    @Param("aSub") String aSub);

  // 작성자 인덱스 조회
  @Select("SELECT Q_IDX FROM QNA_TABLE WHERE QNA_IDX = #{qnaIdx}")
  int getUserIdxByQna(int qnaIdx);
}
