package com.godLife.project.dto.categories;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class shortCutCateDTO {
    @Schema(description = "숏컷 인덱스", example = "1")
    private int shortIdx;

    @Schema(description = "숏컷 이름", example = "취침")
    private String shortName;
}
