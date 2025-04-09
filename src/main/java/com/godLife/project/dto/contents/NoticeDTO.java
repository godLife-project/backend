package com.godLife.project.dto.contents;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoticeDTO {
    @Schema(description = "공지사항 인덱스", example = "1")
    private int noticeIdx;

    @Schema(description = "공지사항 제목", example = "사이트 오픈 안내")
    @NotBlank(message = "공지 제목은 필수입니다.")
    private String noticeTitle;

    @Schema(description = "공지사항 내용", example = "내용이 들어갑니다.")
    @NotBlank(message = "공지 내용은 필수입니다.")
    private String noticeSub;

    @Schema(description = "작성자 인덱스", example = "1")
    private Integer userIdx;

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

    @Schema(description = "팝업 활성화", example = "N or Y (Default = N)")
    private String isPopup; // 'Y' 또는 'N'

    @Schema(description = "팝업 활성 시작시간", example = "2025-02-16 HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime popupStartDate;

    @Schema(description = "팝업 활성 종료시간", example = "2025-02-16 HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime popupEndDate;
}
