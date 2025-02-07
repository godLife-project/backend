package com.godLife.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainDataDTO {

    @Schema(description = "메인 데이터 ID", example = "1")
    private Long idx;

    @Schema(description = "메인 페이지 메시지", example = "Hello, Swagger!")
    private String message;
}