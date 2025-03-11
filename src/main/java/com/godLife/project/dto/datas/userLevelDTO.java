package com.godLife.project.dto.datas;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class userLevelDTO {
    @Schema(description = "등급 인덱스", example = "1")
    private int lvIdx;

    @Schema(description = "최소 인증 경험치", example = "3")
    private int minExp;

    @Schema(description = "최대 인증 경험치", example = "6")
    private int maxExp;
}
