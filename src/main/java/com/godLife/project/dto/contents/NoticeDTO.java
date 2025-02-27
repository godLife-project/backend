package com.godLife.project.dto.contents;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticeDTO {
    @Schema(description = "공지사항 인덱스", example = "1")
    private int noticeIdx;

    @Schema(description = "공지사항 제목", example = "사이트 오픈 안내")
    private String noticeTitle;

    @Schema(description = "공지사항 내용", example = "내용이 들어갑니다.")
    private String noticeSub;

    @Schema(description = "작성자 인덱스", example = "1")
    private int userIdx;

    @Schema(description = "공지사항 작성일", example = "2025-02-16 HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime noticeDate;

    @Schema(description = "공지사항 수정일", example = "2025-02-16 HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime noticeModify;
}
