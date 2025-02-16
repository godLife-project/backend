package com.godLife.project.dto.infos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PlanReportDTO {
    @Schema(description = "신고자 인덱스", example = "1")
    private int REPORTER_IDX;

    @Schema(description = "루틴 인덱스", example = "1")
    private int PLAN_IDX;

    @Schema(description = "신고할 루틴의 활동", example = "3, 10:30, 샤워, 조깅 후 샤워하기\n" +
            "6, 14:00, 회의, 회의 진행하기\n" +
            "9, 20:00, 운동, 운동 후 스트레칭하기")
    private String RE_PLAN;

    @Schema(description = "신고 사유", example = "신고 사유를 작성합니다.")
    private String REPORT_REASON;

    @Schema(description = "신고 상태", example = "0 : 처리 중, 1 : 완료")
    private int STATUS;
}
