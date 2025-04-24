package com.godLife.project.dto.categories;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TopCateDTO {
    @Schema(description = "탑메뉴 인덱스", example = "1")
    private int topIdx;

    @Schema(description = "탑메뉴 이름", example = "New")
    private String topName;

    @Schema(description = "탑메뉴 api경로", example = "/New")
    private String topAddr;

    @Schema(description = "탑메뉴 정렬순서", example = "1")
    private int ordIdx; // 정렬 순서 컬럼
}
