package com.godLife.project.service.interfaces;

import com.godLife.project.dto.contents.QnaDTO;

public interface QnaService {

  // 1:1 문의 작성
  void createQna(QnaDTO qnaDTO);
}
