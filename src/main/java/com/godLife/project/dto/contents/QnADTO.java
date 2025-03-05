package com.godLife.project.dto.contents;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
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

    @Schema(description = "질문 작성일", example = "2025-02-15")
    @JsonSerialize(using = LocalDateSerializer.class) // 직렬화
    @JsonDeserialize(using = LocalDateDeserializer.class) // 역직렬화
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate qDate;

    @Schema(description = "답변자 인덱스", example = "1(관리자 인덱스)")
    private int aIdx;

    @Schema(description = "답변 내용", example = "답변 내용입니다.")
    private String aSub;

    @Schema(description = "답변 작성일", example = "2025-02-15")
    @JsonSerialize(using = LocalDateSerializer.class) // 직렬화
    @JsonDeserialize(using = LocalDateDeserializer.class) // 역직렬화
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate aDate;

    @Schema(description = "문의 비공개 여부", example = "0 : 공개")
    private int qnaPriv;

    @Schema(description = "비공개시 사용 비번", example = "1234")
    private String privPw;

    @Schema(description = "답변 상태확인", example = "N : 미답변")
    private String isAnswered;
}
