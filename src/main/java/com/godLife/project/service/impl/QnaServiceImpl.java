package com.godLife.project.service.impl;

import com.godLife.project.dto.contents.QnADTO;
import com.godLife.project.mapper.QnaMapper;
import com.godLife.project.service.QnaService;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Service
public class QnaServiceImpl implements QnaService {
  private final QnaMapper qnaMapper;
  public  QnaServiceImpl(QnaMapper qnaMapper) {this.qnaMapper = qnaMapper;}

      // 특정 QnA를 Idx로 조회
  public QnADTO getQnaById(int qnaIdx) {
    return qnaMapper.selectQnaById(qnaIdx);
  }
     // 모든 QnA를 조회
  public List<QnADTO> selectAllQna() {
    return qnaMapper.selectAllQna();
  }

     //  QnA 작성
  public void createQna(QnADTO qna) {
    qnaMapper.insertQna(qna);
  }
     //  QnA 수정
  public void updateQna(QnADTO qna) {
    qnaMapper.updateQna(qna);
  }
     //  QnA 삭제
  public void deleteQna(int qnaIdx) {
    qnaMapper.deleteQna(qnaIdx);
  }

     // QnA 검색
     public List<QnADTO> searchQna(String query) {
       // query가 null이거나 비어 있으면 빈 리스트 반환
       if (query == null || query.trim().isEmpty()) {
         return Collections.emptyList();
       }
       return qnaMapper.searchQna(query); // Mapper를 통해 DB에서 검색
     }
}
