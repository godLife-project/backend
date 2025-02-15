package com.godLife.project.dto.categories;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AuthorityCateDTO {
    @Schema(description = "권한 idx", example = "1")
    private int authorityIdx;
    @Schema(description = "권한 이름", example = "유저")
    private String authorityName;
    @Schema(description = "권한 설명", example = "관리자 페이지 접근 불가")
    private String authorityDescription;
}
