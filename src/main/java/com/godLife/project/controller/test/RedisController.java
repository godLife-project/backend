package com.godLife.project.controller.test;

import com.godLife.project.service.impl.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {

  @Autowired
  private RedisService redisService;

  @PostMapping("/save")
  public String saveData(@RequestParam String key, @RequestParam String value) {
    redisService.saveData(key, value, 3600);
    return "Data saved to Redis";
  }

  @GetMapping("/get")
  public String getData(@RequestParam String key) {
    return redisService.getData(key);
  }

  @PostMapping("/delete")
  public String deleteData(@RequestParam String key) {
    redisService.deleteData(key);
    return "Data deleted from Redis";
  }
}
