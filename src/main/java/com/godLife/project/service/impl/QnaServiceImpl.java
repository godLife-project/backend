package com.godLife.project.service.impl;

import com.godLife.project.dto.contents.QnADTO;
import com.godLife.project.mapper.QnaMapper;
import com.godLife.project.service.QnaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Slf4j
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
  public int createQna(QnADTO qna) {
    int result = qnaMapper.insertQna(qna);
    if (result > 0) {
      return 200; // 성공
    } else {
      return 500; // 실패
    }
  }
     //  QnA 수정
  public int updateQna(QnADTO qnaDTO) {
    qnaMapper.updateQna(qnaDTO);
    int userIdx = qnaDTO.getQIdx();
    int qnaIdx = qnaDTO.getQnaIdx();

    // 작성자만 수정 가능
    if (userIdx != qnaMapper.getUserIdxByQna(qnaIdx)) {
      return 403; // Forbidden
    }
    try {
      // QnA 수정
      return 200; // 성공
    } catch (Exception e) {
      log.error("Error modifying QnA: ", e);
      return 500; // Internal Server Error: 수정 실패
    }
  }

  // QnA 삭제
  public int deleteQna(int qnaIdx, int userIdx) {
    // 작성자 인덱스 조회
    int writerIdx = qnaMapper.getUserIdxByQna(qnaIdx);

    // 작성자만 삭제 가능
    if (userIdx != writerIdx) {
      return 403; // Forbidden
    }

    try {
      // QnA 삭제
      qnaMapper.deleteQna(qnaIdx);
      return 200; // 성공
    } catch (Exception e) {
      // 오류 발생 시 처리
      return 500; // Internal Server Error
    }
  }


     // QnA 검색
     public List<QnADTO> searchQna(String query) {
       // query가 null이거나 비어 있으면 빈 리스트 반환
       if (query == null || query.trim().isEmpty()) {
         return Collections.emptyList();
       }
       return qnaMapper.searchQna(query); // Mapper를 통해 DB에서 검색
     }

  public void saveAnswer(int qnaIdx, int aIdx, String aSub) {
    // 답변 등록/수정
    qnaMapper.updateAnswer(qnaIdx, aIdx, aSub);
  }
}
