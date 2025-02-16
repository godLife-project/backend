package com.godLife.project.dto.infos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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
}
