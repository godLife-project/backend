package com.godLife.project.dto.infos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VerifyDTO {
    @Schema(description = "인증 인덱스", example = "1")
    private int verifyIdx;

    @Schema(description = "루틴 인덱스", example = "1 [루틴 인덱스 값이 들어오면 챌린지 인덱스 null]")
    private int planIdx;

    @Schema(description = "챌린지 인덱스", example = "1[챌린지 인덱스 값이 들어오면 루틴 인덱스 null]")
    private int challIdx;

    @Schema(description = "유저 인덱스", example = "1")
    private int userIdx;

    @Schema(description = "인증한 날짜", example = "2025/02/16 HH:mm:ss")
    private LocalDate verifyDate;

    @Schema(description = "경과 시간(챌린지용)", example = "120")
    private int elapsedTime;

    @Schema(description = "사진 경로", example = "사진 경로")
    private String imagePath;
}
