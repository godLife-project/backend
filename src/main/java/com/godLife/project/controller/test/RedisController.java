package com.godLife.project.controller.test;

import com.godLife.project.dto.categories.TopCateDTO;
import com.godLife.project.service.impl.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RedisController {

  @Autowired
  private RedisService redisService;

  @PostMapping("/save")
  public String saveData(@RequestParam String key, @RequestParam String value) {
    redisService.saveStringData(key, value, 'h', 1);
    return "Data saved to Redis";
  }

  @GetMapping("/get")
  public String getData(@RequestParam String key) {
    return redisService.getStringData(key);
  }

  @GetMapping("/get-top")
  public List<TopCateDTO> topMenu(@RequestParam String key) { return redisService.getListData(key, TopCateDTO.class); }

  @DeleteMapping("/delete")
  public String deleteData(@RequestParam String key) {
    redisService.deleteData(key);
    return "Data deleted from Redis";
  }
}
