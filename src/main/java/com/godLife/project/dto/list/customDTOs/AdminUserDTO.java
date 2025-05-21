package com.godLife.project.dto.list.customDTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminUserDTO {

    @Schema(description = "유저 인덱스",example = "1")
    private int userIdx;

    @Schema(description = "유저 이름", example = "홍길동")
    private String userName;

    @Schema(description = "유저 아이디", example = "gildong")
    private String userId;

    @Schema(description = "유저 닉네임", example = "길동짱")
    private String userNick;

    @Schema(description = "닉네임 태그", example = "#1")
    private String nickTag;

    @Schema(description = "유저 이메일", example = "rlfehd@naver.com")
    private String userEmail;

    @Schema(description = "유저 가입일", example = "2025-02-14")
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime userJoin;

    @Schema(description = "신고 횟수", example = "1")
    private int reportCount;


    private int planCount;

}
