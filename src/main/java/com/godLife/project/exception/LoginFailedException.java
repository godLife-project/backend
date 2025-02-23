package com.godLife.project.exception;


/*
        해당 코드 현재 사용 안하는중!!!!!

 */
public class LoginFailedException extends RuntimeException {
    public LoginFailedException(String message) {
        super(message);  // 에러 메시지를 부모 클래스에 전달
    }
}
