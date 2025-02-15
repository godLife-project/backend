package com.godLife.project.controller;

import com.godLife.project.service.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final TestService testService;

    public HomeController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "hello, fucking thymeleaf!");
        List<String> testDTOS = testService.getJobName();
        System.out.println(testDTOS);
        model.addAttribute("testDTOS", testDTOS);
        return "index";
    }
}
