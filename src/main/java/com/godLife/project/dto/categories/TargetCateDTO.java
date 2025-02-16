package com.godLife.project.dto.categories;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TargetCateDTO {
    @Schema(description = "관심사 인덱스", example = "1")
    private int targetIdx;

    @Schema(description = "관심사 이름", example = "미라클 모닝")
    private String targetName;
}
