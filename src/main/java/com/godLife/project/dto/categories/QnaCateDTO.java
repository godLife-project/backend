package com.godLife.project.dto.categories;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class QnaCateDTO {
  @Schema(description = "QNA 카테고리 인덱스", example = "1")
  private int qnaCategoryIdx;  // QNA_CATEGORY 테이블의 QNA_CATEGORY_IDX

  @Schema(description = "QNA 카테고리 이름", example = "기술")
  private String qnaCategoryName;  // QNA_CATEGORY 테이블의 QNA_CATEGORY_NAME
}
