package com.godLife.project.enums;

public enum ChallengeState {
    PUBLISHED("게시중"),
    IN_PROGRESS("진행중"),
    COMPLETED("완료됨"),
    FAILED("실패");

    private final String state;

    ChallengeState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public static ChallengeState fromString(String state) {
        for (ChallengeState challengeState : ChallengeState.values()) {
            if (challengeState.state.equalsIgnoreCase(state)) {
                return challengeState;
            }
        }
        throw new IllegalArgumentException("Invalid state: " + state);
    }
}

