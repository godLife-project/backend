package com.godLife.project.dto.categories;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobEtcCateDTO {
    @Schema(description = "기타 직업 인덱스", example = "1")
    private int idx;

    @Schema(description = "생성된 루틴 인덱스", example = "1")
    private int planIdx;

    @Schema(description = "기타 직업 이름", example = "무직")
    private String name;

    @Schema(description = "기타 직업 아이콘", example = "coffee")
    private String iconKey;

    @Schema(description = "기타 직업 생성일", example = "coffee")
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "아이콘 테이블의 아이콘 코드", example = "Sunrise")
    private String icon;
    @Schema(description = "아이콘 테이블의 아이콘 색상", example = "#FF9500")
    private String color;
}
