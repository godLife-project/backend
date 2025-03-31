package com.godLife.project.service.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean verifyPassword(String userPw, String encryptedPassword) {

        return passwordEncoder.matches(userPw, encryptedPassword);
    }
}
