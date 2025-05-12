package com.godLife.project.dto.response.qna;

import lombok.Data;

import java.util.List;

@Data
public class QnaParent {
  private int parentIdx;
  private String parentName;

  List<QnaChild> childCategory;
}
