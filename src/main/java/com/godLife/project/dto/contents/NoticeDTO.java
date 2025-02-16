package com.godLife.project.dto.contents;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticeDTO {
    @Schema(description = "공지사항 인덱스", example = "1")
    private int NOTICE_IDX;

    @Schema(description = "공지사항 제목", example = "사이트 오픈 안내")
    private String NOTICE_TITLE;

    @Schema(description = "공지사항 내용", example = "내용이 들어갑니다.")
    private String NOTICE_SUB;

    @Schema(description = "작성자 인덱스", example = "1")
    private int USER_IDX;

    @Schema(description = "공지사항 작성일", example = "2025-02-16 HH:mm:ss")
    private LocalDateTime NOTICE_DATE;

    @Schema(description = "공지사항 수정일", example = "2025-02-16 HH:mm:ss")
    private LocalDateTime NOTICE_MODIFY;
}
