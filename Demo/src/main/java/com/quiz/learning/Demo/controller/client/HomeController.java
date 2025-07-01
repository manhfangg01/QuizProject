package com.quiz.learning.Demo.controller.client;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class HomeController {
    @GetMapping("/")
    public String fetch() {
        return "Welcome home !";
    }
}
