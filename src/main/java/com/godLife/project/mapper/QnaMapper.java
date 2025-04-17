package com.godLife.project.mapper;

import com.godLife.project.dto.contents.QnaDTO;
import com.godLife.project.dto.qnaWebsocket.QnaMatchedListDTO;
import com.godLife.project.dto.qnaWebsocket.QnaWaitListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QnaMapper {

  // 1:1 문의 작성
  void createQna(QnaDTO qnaDTO);

  // 'WAIT' 상태 문의의 qnaIdx 가져오기
  @Select("SELECT QNA_IDX FROM QNA_TABLE WHERE QNA_STATUS = 'WAIT'")
  List<Integer> getlistWaitQnaIdx();

  // 'WAIT' 상태 문의의 리스트용 정보 가져오기
  @Select("SELECT QNA_IDX, Q_USER_IDX, TITLE, CREATED_AT, MODIFIED_AT, CATEGORY, QNA_STATUS FROM QNA_TABLE WHERE QNA_STATUS = 'WAIT'")
  List<QnaWaitListDTO> getlistAllWaitQna();

  // 매칭된 문의 리스트 조회
  List<QnaMatchedListDTO> getlistAllMatchedQna(int adminIdx);
}
