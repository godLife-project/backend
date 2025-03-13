package com.godLife.project.dto.datas;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.godLife.project.valid.annotation.UniqueUserEmail;
import com.godLife.project.valid.annotation.UniqueUserId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {


    @Schema(description = "유저 idx", example = "1")
    private int userIdx;

    @Schema(description = "유저 이름", example = "홍길동")
    @NotBlank(message = "{joinUser.userName.notBlank}")
    @Size(min=3, max=15, message = "{joinUser.userName.size}")
    private String userName;

    @Schema(description = "유저 아이디", example = "Hong123")
    @NotBlank(message = "{joinUser.userId.notBlank}")
    @Size(min = 4, max = 15, message = "{joinUser.userId.size}")
    @Pattern(regexp="[a-zA-Z0-9]*", message = "{joinUser.userId.pattern}")
    @UniqueUserId
    private String userId;

    @Schema(description = "유저 비밀번호", example = "1234")
    @NotBlank(message = "{joinUser.userPw.notBlank}")
    @Size(min = 4, max = 15, message = "{joinUser.userPw.size}")
    @Pattern(regexp="[a-zA-Z0-9]*", message = "{joinUser.userPw.pattern}")
    private String userPw;

    @Schema(description = "유저 닉네임", example = "의적단")
    @NotBlank(message = "{joinUser.userNick.notBlank}")
    @Size(min=2, max=15, message = "{joinUser.userNick.size}")
    @Pattern(regexp="[가-힣a-zA-Z0-9-_]*", message = "{joinUser.userNick.pattern}")
    private String userNick;

    @Schema(description = "닉네임 중복 태그", example = "#1")
    @Pattern(regexp="#[가-힣a-zA-Z0-9-_]+", message = "{joinUser.nickTag.pattern}")
    private String nickTag;

    @Schema(description = "유저 이메일", example = "hong@example.com")
    @Email(message = "{joinUser.userEmail.email}")
    @UniqueUserEmail
    private String userEmail;

    @Schema(description = "현재 직업 idx", example = "1")
    @Min(value = 1, message = "{joinUser.jobIdx.min}")
    private int jobIdx;

    @Schema(description = "초기 관심사 idx", example = "1")
    @Min(value = 1, message = "{joinUser.targetIdx.min}")
    private int targetIdx;

    @Schema(description = "유저 전화번호", example = "010-1234-5678")
    @Pattern(
            regexp = "^(010-\\d{4}-\\d{4})|(02-\\d{3,4}-\\d{4})|([0-9]{3}-\\d{3,4}-\\d{4})$",
            message = "{joinUser.userPhone.pattern}"
    )
    @NotBlank(message = "{joinUser.userPhone.notBlank}")
    private String userPhone;

    @Schema(description = "유저 성별", example = "1")
    @Min(value = 1, message = "{joinUser.userGender.min}")
    private int userGender;

    @Schema(description = "유저 가입일", example = "2025-02-14")
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime userJoin;

    @Schema(description = "유저 최대 성취도", example = "3")
    private int maxFireIdx;

    @Schema(description = "유저 권한", example = "1")
    private int authorityIdx;

    @Schema(description = "유저 콤보", example = "1")
    private int combo;

    @Schema(description = "유저 경험치", example = "1")
    private double userExp;

    @Schema(description = "유저 레벨", example = "1")
    private int userLv;

    @Schema(description = "관리자 여부", example = "false")
    private boolean roleStatus = false;
}
