package com.godLife.project.dto.contents;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.godLife.project.dto.infos.ChallengeJoinDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChallengeDTO {
    @Schema(description = "챌린지 인덱스", example = "1")
    private Long  challIdx;

    @Schema(description = "챌린지 제목", example = "하루 독서 2시간 챌린지")
    private String challTitle;

    @Schema(description = "챌린지 설명", example = "나랏말싸미 듕귁에 달아.. 요즘 독해력이 매우 떨어지고 있습니다.")
    private String challDescription;

    @Schema(description = "챌린지 관련 카테고리 인덱스", example = "1")
    private int challCategoryIdx;

    @Schema(description = "최소 참여 시간 제한", example = "1")
    private int minParticipationTime;

    @Schema(description = "총 클리어 시간", example = "24")
    private int totalClearTime;

    @Schema(description = "최대 참여 인원", example = "8")
    private int maxParticipants;

    @Schema(description = "챌린지 시작 시간", example = "2025-02-15 HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime challStartTime;

    @Schema(description = "챌린지 종료 시간", example = "2025-02-16 HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime challEndTime;

    @Schema(description = "챌린지 작성일", example = "2025-02-16 HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime challCreatedAt;

    @Schema(description = "챌린지 상태", example = "게시중")
    private String challState;

    @Schema(description = "챌린지 형태", example = "관리자 개입형")
    private int userJoin;

    @Schema(description = "챌린지 유지시간", example = "7일")
    private Integer duration;

    private int currentParticipants; // 현재 참여자 수

    // 참가자 정보 리스트
    private List<ChallengeJoinDTO> participants;
}
