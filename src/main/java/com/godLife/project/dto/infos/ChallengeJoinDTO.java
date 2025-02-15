package com.godLife.project.dto.infos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChallengeJoinDTO {
    @Schema(description = "챌린지 참여 고유 인덱스", example = "1")
    private int challJoinIdx;
    @Schema(description = "참여 챌린지 인덱스", example = "1")
    private int challIdx;
    @Schema(description = "참여 유저 인덱스", example = "1")
    private int userIdx;
    @Schema(description = "예정 활동 시작 시간", example = "08:00")
    private LocalDateTime startTime;
    @Schema(description = "예정 활동 종료 시간", example = "10:00")
    private LocalDateTime endTime;
    @Schema(description = "활동명", example = "조깅")
    private String activity;
}
