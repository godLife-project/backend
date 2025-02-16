package com.godLife.project.dto.infos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LikeDTO {
    @Schema(description = "유저 인덱스", example = "1")
    private int USER_IDX;

    @Schema(description = "루틴 인덱스", example = "1")
    private int PLAN_IDX;

    @Schema(description = "추천 시간 로그", example = "2025-02-16 HH:mm:ss")
    private LocalDateTime LIKE_TIMESTAMP;
}
