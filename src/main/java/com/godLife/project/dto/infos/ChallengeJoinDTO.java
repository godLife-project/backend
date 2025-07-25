package com.godLife.project.dto.infos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChallengeJoinDTO {
    @Schema(description = "챌린지 참여 고유 인덱스", example = "1")
    private int challJoinIdx;

    @Schema(description = "참여 챌린지 인덱스", example = "1")
    private Long challIdx;

    @Schema(description = "참여 유저 인덱스", example = "1")
    private int userIdx;

    @Schema(description = "활동명", example = "조깅")
    private String activity;

    @Schema(description = "활동 목표시간", example = "2시간")
    private int activityTime;

    @Schema(description = "유저 닉네임", example = "유저1")
    private String userNick;
}
