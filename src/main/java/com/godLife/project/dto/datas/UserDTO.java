package com.godLife.project.dto.datas;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@Data
public class UserDTO {


    @Schema(description = "유저 idx", example = "1")
    private int userIdx;

    @Schema(description = "유저 이름", example = "홍길동")
    private String userName;

    @Schema(description = "유저 아이디", example = "Hong123")
    private String userId;

    @Schema(description = "유저 비밀번호", example = "1234")
    private String userPw;

    @Schema(description = "유저 닉네임", example = "의적단")
    private String userNick;

    @Schema(description = "유저 이메일", example = "hong@example.com")
    private String userEmail;

    @Schema(description = "현재 직업 idx", example = "1")
    private int jobIdx;

    @Schema(description = "초기 관심사 idx", example = "1")
    private int targetIdx;

    @Schema(description = "유저 전화번호", example = "010-1234-5678")
    private String userPhone;

    @Schema(description = "유저 성별", example = "0")
    private int userGender;

    @Schema(description = "유저 가입일", example = "2025-02-14")
    private LocalDate userJoin;

    @Schema(description = "유저 최대 성취도", example = "3")
    private int maxFireIdx;

    @Schema(description = "유저 권한", example = "1")
    private int authorityIdx;

    // 기본 생성자 추가 (Spring이 JSON을 객체로 변환할 때 필요)
    public UserDTO() {}
}
