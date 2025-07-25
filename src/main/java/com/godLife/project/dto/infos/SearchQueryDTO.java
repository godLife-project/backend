package com.godLife.project.dto.infos;

import lombok.Data;

@Data
public class SearchQueryDTO {
  private String query;
  private String faqTitle;     // 내부에서 query를 복사해서 사용
  private String faqAnswer;
  private String faqCategoryName;
}
