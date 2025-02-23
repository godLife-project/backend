package com.godLife.project.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Iterator;

@RestController
public class TestController {
  @GetMapping("/test2")
  public String TestP() {
    String name = SecurityContextHolder.getContext().getAuthentication().getName();

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Iterator<? extends GrantedAuthority> iter = authorities.iterator();
    GrantedAuthority auth = iter.next();
    String role = auth.getAuthority();

    return "Test2 Conroller : " + name + " 권한 : " + role;
  }
}
