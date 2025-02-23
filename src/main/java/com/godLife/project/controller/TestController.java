package com.godLife.project.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestController {
  @GetMapping("/test1")
  public ResponseEntity<Map<String, String>> Test() {
    Map<String, String> response = new HashMap<>();
    response.put("message", "hello!!!!!");

    return ResponseEntity.ok(response);
  }

  @GetMapping("/test2")
  public ResponseEntity<Map<String, String>> TestP() {
    Map<String, String> response = new HashMap<>();
    response.put("message", "You have JWT access Token");

    return ResponseEntity.ok(response);
  }
}
