package com.godLife.project.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {

    private final String type;

    public UnauthorizedException(String type, String message) {
        super(message);
        this.type = type;
    }

  // 🔧 static factory method 추가
    public static UnauthorizedException of(String type, String message) {
        return new UnauthorizedException(type, message);
    }
}
