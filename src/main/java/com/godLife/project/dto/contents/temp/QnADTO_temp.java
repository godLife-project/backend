package com.godLife.project.dto.contents.temp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QnADTO_temp {
    @Schema(description = "qna 고유 인덱스", example = "1")
    private Long qnaIdx;

    @Schema(description = "qna 작성자 인덱스", example = "1")
    private Long qIdx;

    @Schema(description = "qna 질문 제목 (내용)", example = "계정문의")
    private String qTitle;

    @Schema(description = "문의 일자", example = "2025-02-16 HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
    private LocalDateTime qDate;

    @Schema(description = "질문 카테고리", example = "계정관련")
    private Integer qCategory;
}
