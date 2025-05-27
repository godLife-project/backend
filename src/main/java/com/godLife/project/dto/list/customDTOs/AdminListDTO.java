package com.godLife.project.dto.list.customDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdminListDTO {
    @Schema(description = "유저 인덱스",example = "1")
    private int userIdx;

    @Schema(description = "유저 이름", example = "홍길동")
    private String userName;

    private int authorityIdx;      // 반드시 int로 선언했다면 null 값 있을 경우 오류 발생
    private String authorityName;
}

