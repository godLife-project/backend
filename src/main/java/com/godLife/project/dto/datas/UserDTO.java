package com.godLife.project.dto.datas;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

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
    private String userId;

    @Schema(description = "유저 비밀번호", example = "1234")
    @NotBlank(message = "{joinUser.userPw.notBlank}")
    @Size(min = 4, max = 15, message = "{joinUser.userPw.size}")
    @Pattern(regexp="[a-zA-Z0-9]*", message = "{joinUser.userPw.pattern}")
    private String userPw;

    @Schema(description = "비밀번호 확인", example = "1234")
    private String userPw2;

    @Schema(description = "유저 닉네임", example = "의적단")
    @NotBlank(message = "{joinUser.userNick.notBlank}")
    @Size(min=2, max=15, message = "{joinUser.userNick.size}")
    @Pattern(regexp="[가-힣a-zA-Z0-9-_]*", message = "{joinUser.userNick.pattern}")
    private String userNick;

    @Schema(description = "유저 이메일", example = "hong@example.com")
    @Email(message = "{joinUser.userEmail.email}")
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

    @Schema(description = "유저 성별", example = "0")
    @Min(value = 1, message = "{joinUser.userGender.min}")
    private int userGender;

    @Schema(description = "유저 가입일", example = "2025-02-14")
    private LocalDate userJoin;

    @Schema(description = "유저 최대 성취도", example = "3")
    private int maxFireIdx;

    @Schema(description = "유저 권한", example = "1")
    private int authorityIdx;
}
