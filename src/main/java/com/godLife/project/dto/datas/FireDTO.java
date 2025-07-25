package com.godLife.project.dto.datas;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FireDTO {
    @Schema(description = "등급 인덱스", example = "1")
    private Long lvIdx;

    @Schema(description = "최소 인증 경험치", example = "3")
    private int minExp;

    @Schema(description = "최대 인증 경험치", example = "6")
    private int maxExp;

    @Schema(description = "등급 이름", example = "불씨")
    private String fireName;

    @Schema(description = "등급 색상", example = "#FF4500")
    private String fireColor;

    @Schema(description = "등급 효과", example = "작은 불씨가 피어오름")
    private String fireEffect;
}
