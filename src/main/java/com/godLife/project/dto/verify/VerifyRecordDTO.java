package com.godLife.project.dto.verify;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VerifyRecordDTO {
    @Schema(description = "인증 인덱스", example = "1")
    private int verifyIdx;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long elapsedMinutes;
    private String activity; // chall_join 테이블
}
