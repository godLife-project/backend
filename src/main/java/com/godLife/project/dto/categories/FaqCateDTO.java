package com.godLife.project.dto.categories;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FaqCateDTO {
  @Schema(description = "FAQ 카테고리 인덱스", example = "1")
  private int faqCategoryIdx;  // FAQ_CATEGORY 테이블의 FAQ_CATEGORY_IDX

  @Schema(description = "FAQ 카테고리 이름", example = "기술")
  private String faqCategoryName;  // FAQ_CATEGORY 테이블의 FAQ_CATEGORY_NAME
}
