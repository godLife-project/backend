package com.godLife.project.dto.categories;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ChallengeCateDTO {
    @Schema(description = "챌린지 관련 카테고리 idx", example = "1")
    private int challCateIdx;

    @Schema(description = "카테고리 이름", example = "전체")
    private String challName;

    @Schema(description = "챌린지 아이콘", example = "coffee")
    private String iconKey;

    @Schema(description = "아이콘 테이블의 아이콘 코드", example = "Sunrise")
    private String icon;

    @Schema(description = "아이콘 테이블의 아이콘 색상", example = "#FF9500")
    private String color;
}
