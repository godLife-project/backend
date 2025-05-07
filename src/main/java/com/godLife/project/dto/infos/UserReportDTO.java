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
public class UserReportDTO {
    @Schema(description = "신고자 인덱스", example = "1")
    private int reporterIdx;

    @Schema(description = "피신고자 인덱스", example = "2")
    private int reportedIdx;

    @Schema(description = "신고 사유", example = "신고 사유를 작성합니다.")
    private String reportReason;

    @Schema(description = "신고 상태", example = "0 : 처리 중, 1 : 완료")
    private int status;

    @Schema(description = "추천 시간 로그", example = "2025-02-16 HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reportDate;

    // 추가 필드
    private String reporterNick;
    private String reportedNick;
}
