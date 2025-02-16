package com.godLife.project.dto.categories;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class JobCateDTO {
    @Schema(description = "직업 인덱스", example = "1")
    private int jobIdx;

    @Schema(description = "직업 이름", example = "무직")
    private String jobName;
}
