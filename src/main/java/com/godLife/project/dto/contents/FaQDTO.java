package com.godLife.project.dto.contents;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FaQDTO {
  @Schema(description = "FAQ 인덱스", example = "1")
  private int faqIdx;

  @Schema(description = "FAQ 제목", example = "FAQ 제목입니다")
  private String faqTitle;

  @Schema(description = "FAQ 답변", example = "FAQ 답변(내용)입니다")
  private String faqAnswer;

  @Schema(description = "FAQ 카테고리", example = "계정관련")
  private Integer faqCategory;

  @Schema(description = "FAQ 카테고리 이름", example = "기술")
  private String faqCategoryName;
}
