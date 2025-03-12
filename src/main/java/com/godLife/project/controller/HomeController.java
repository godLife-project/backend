package com.godLife.project.controller;

import com.godLife.project.service.interfaces.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final TestService testService;

    public HomeController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "hello, thymeleaf!");
        return "index";
    }
}
