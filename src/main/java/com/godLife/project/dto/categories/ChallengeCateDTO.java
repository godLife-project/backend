package com.godLife.project.dto.categories;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ChallengeCateDTO {
    @Schema(description = "챌린지 관련 카테고리 idx", example = "1")
    private int challCategoryIdx;
    @Schema(description = "카테고리 이름", example = "전체")
    private String challName;
}
