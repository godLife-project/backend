package com.godLife.project.dto.contents;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChallengeDTO {
    @Schema(description = "챌린지 인덱스", example = "1")
    private int challIdx;

    @Schema(description = "챌린지 제목", example = "하루 독서 2시간 챌린지")
    private String challTitle;

    @Schema(description = "챌린지 설명", example = "나랏말싸미 듕귁에 달아.. 요즘 독해력이 매우 떨어지고 있습니다.")
    private String challDescription;

    @Schema(description = "챌린지 관련 카테고리 인덱스", example = "1")
    private int challCategoryIdx;

    @Schema(description = "최소 참여 시간 제한", example = "1")
    private int minParticipationTime;

    @Schema(description = "총 클리어 시간", example = "24")
    private int totalClearTime;

    @Schema(description = "최대 참여 인원", example = "8")
    private int maxParticipants;

    @Schema(description = "챌린지 시작 시간", example = "2025-02-15 HH:mm:ss")
    private LocalDateTime challStartTime;

    @Schema(description = "챌린지 종료 시간", example = "2025-02-16 HH:mm:ss")
    private LocalDateTime challEndTime;
}
