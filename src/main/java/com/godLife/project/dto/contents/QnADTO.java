package com.godLife.project.dto.contents;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class QnADTO {
    @Schema(description = "문의 인덱스", example = "1")
    private int qnaIdx;

    @Schema(description = "질문자 인덱스", example = "1")
    private int qIdx;

    @Schema(description = "질문 제목", example = "질문 제목입니다.")
    private String qTitle;

    @Schema(description = "질문 내용", example = "질문 내용입니다.")
    private String qSub;

    @Schema(description = "질문 작성일", example = "2025/02/15")
    private LocalDate qDate;

    @Schema(description = "답변자 인덱스", example = "1(관리자 인덱스)")
    private int aIdx;

    @Schema(description = "답변 내용", example = "답변 내용입니다.")
    private String aSub;

    @Schema(description = "답변 작성일", example = "2025/02/15")
    private LocalDate aDate;

    @Schema(description = "문의 비공개 여부", example = "0 : 공개")
    private int qnaPriv;

    @Schema(description = "비공개시 사용 비번", example = "1234")
    private String privPw;
}
