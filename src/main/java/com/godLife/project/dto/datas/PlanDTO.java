package com.godLife.project.dto.datas;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlanDTO {
    @Schema(description = "루틴 인덱스", example = "1")
    private int planIdx;

    @Schema(description = "작성자 인덱스", example = "1")
    private int userIdx;

    @Schema(description = "루틴 제목", example = "루틴 제목입니다.")
    private String planTitle;

    @Schema(description = "목표 개월 수", example = "3 : 시작일로부터 3개월이 종료일이 됨.")
    private int endTo;

    @Schema(description = "관심 카테고리", example = "1 : 목표 카테고리 인덱스")
    private int targetIdx;

    @Schema(description = "루틴 계획표", example = "1, 09:00, 기상, 기상 후 정신차리기")
    private String planSub;

    @Schema(description = "루틴 작성일", example = "2025/02/14")
    private LocalDateTime planSubDate;

    @Schema(description = "루틴 수정일", example = "2025/02/14")
    private LocalDateTime planSubMod;

    @Schema(description = "루틴 시작일", example = "2025/02/14")
    private LocalDateTime planSubStart;

    @Schema(description = "루틴 종료일", example = "2025/05/14")
    private LocalDateTime planSubEnd;

    @Schema(description = "조회 수", example = "1")
    private int viewCount;

    @Schema(description = "인증 횟수", example = "1")
    private int certCount;

    @Schema(description = "추천 수", example = "1")
    private int likeCount;

    @Schema(description = "공개 여부", example = "1 : 공개")
    private int isShared;

    @Schema(description = "활성 여부", example = "0 : 비활성화")
    private int isActive;

    @Schema(description = "종료 여부", example = "0 : 진행중")
    private int isCompleted;

    @Schema(description = "완료 후기", example = "후기 작성입니다.")
    private String review;
}
