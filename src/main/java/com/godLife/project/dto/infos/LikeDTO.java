package com.godLife.project.dto.infos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LikeDTO {
    @Schema(description = "유저 인덱스", example = "1")
    private int userIdx;

    @Schema(description = "루틴 인덱스", example = "1")
    private int planIdx;

    @Schema(description = "추천 시간 로그", example = "2025-02-16 HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime likeTimestamp;
}
