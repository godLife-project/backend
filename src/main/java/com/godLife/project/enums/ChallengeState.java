package com.godLife.project.enums;

import java.util.Arrays;

public enum ChallengeState {
    PUBLISHED("PUBLISHED", "게시중"),
    IN_PROGRESS("IN_PROGRESS", "진행중"),
    COMPLETED("COMPLETED", "완료됨"),
    END("END", "종료");

    private final String code;   // DB 및 내부 처리용
    private final String label;  // 사용자 표시용

    ChallengeState(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static ChallengeState fromCode(String code) {
        return Arrays.stream(values())
                .filter(state -> state.code.equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown code: " + code));
    }

    public static ChallengeState fromLabel(String label) {
        return Arrays.stream(values())
                .filter(state -> state.label.equalsIgnoreCase(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown label: " + label));
    }
}

