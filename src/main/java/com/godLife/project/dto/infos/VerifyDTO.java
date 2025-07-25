package com.godLife.project.dto.infos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VerifyDTO {
    @Schema(description = "인증 인덱스", example = "1")
    private int verifyIdx;

    @Schema(description = "루틴 인덱스", example = "1 [루틴 인덱스 값이 들어오면 챌린지 인덱스 null]")
    private int activityIdx;

    @Schema(description = "챌린지 인덱스", example = "1[챌린지 인덱스 값이 들어오면 루틴 인덱스 null]")
    private Long challIdx;

    @Schema(description = "유저 인덱스", example = "1")
    private int userIdx;

    @Schema(description = "인증한 날짜", example = "2025/02/16 HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime verifyDate;

    @Schema(description = "예정 활동 시작 시간", example = "2025-04-07T08:00:00")
    @JsonSerialize(using = LocalTimeSerializer.class) // 직렬화
    @JsonDeserialize(using = LocalTimeDeserializer.class) // 역직렬화
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    @Schema(description = "예정 활동 종료 시간", example = "2025-04-07T08:00:00")
    @JsonSerialize(using = LocalTimeSerializer.class) // 직렬화
    @JsonDeserialize(using = LocalTimeDeserializer.class) // 역직렬화
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;

    @Schema(description = "경과 시간(챌린지용)", example = "120")
    private int elapsedTime;

}
