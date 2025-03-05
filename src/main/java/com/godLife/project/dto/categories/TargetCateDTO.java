package com.godLife.project.dto.categories;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TargetCateDTO {
    @Schema(description = "관심사 인덱스", example = "1")
    private int idx;

    @Schema(description = "관심사 이름", example = "미라클 모닝")
    private String name;

    @Schema(description = "관심사 아이콘", example = "sunrise")
    private String iconKey;

    @Schema(description = "아이콘 테이블의 아이콘 코드", example = "Sunrise")
    private String icon;
    @Schema(description = "아이콘 테이블의 아이콘 색상", example = "#FF9500")
    private String color;
}
