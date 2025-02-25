package com.godLife.project.enums;

public enum ChallengeState {
    PUBLISHED("게시중"),
    IN_PROGRESS("진행중"),
    COMPLETED("완료됨"),
    FAILED("실패");

    private final String description;

    ChallengeState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

