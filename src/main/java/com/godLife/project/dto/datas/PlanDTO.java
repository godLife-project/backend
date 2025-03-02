package com.godLife.project.dto.datas;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PlanDTO {
    @Schema(description = "루틴 인덱스", example = "1")
    private int planIdx;

    @Schema(description = "작성자 인덱스", example = "1")
    private int userIdx;

    @Schema(description = "루틴 제목", example = "루틴 제목입니다.")
    @NotBlank(message = "{writePlan.planTitle.notBlank}")
    private String planTitle;

    @Schema(description = "목표 개월 수", example = "7 : 시작일로부터 7일 후가 종료일이 됨.")
    @Min(value = 7, message = "{writePlan.endTo.min}")
    private int endTo;

    @Schema(description = "관심 카테고리", example = "1 : 목표 카테고리 인덱스")
    @Min(value = 1, message = "{writePlan.targetIdx.min}")
    private int targetIdx;

    @Schema(description = "직업 카테고리", example = "1 : 직업 카테고리 인덱스")
    private int jobIdx;

    @Schema(description = "루틴 중요도", example = "1 : 최하단 혹은 제일 마지막에 배치")
    @Min(value = 1, message = "{writePlan.planImp.min}")
    @Max(value = 10, message = "{writePlan.planImp.max}")
    private int planImp;

    @Schema(description = "루틴 작성일", example = "2025-02-14")
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime planSubDate;

    @Schema(description = "루틴 수정일", example = "2025-02-14")
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime planSubMod;

    @Schema(description = "루틴 시작일", example = "2025-02-14 HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime planSubStart;

    @Schema(description = "루틴 종료일", example = "2025-02-21 HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime planSubEnd;

    @Schema(description = "조회 수", example = "1")
    private int viewCount;

    @Schema(description = "인증 경험치", example = "1")
    private int certExp;

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

    @Schema(description = "삭제 상태", example = "0: 삭제안함 1: 삭제됨")
    private int isDeleted;

    @Schema(description = "활동 리스트", example = "활동들")
    @Size(min = 1, message = "{writePlan.activities.size}")
    @Valid
    private List<ActivityDTO> activities;

    @Schema(description = "활동 삭제 인덱스", example = "1, 2, 3")
    private List<Integer> deleteActivityIdx;
}
