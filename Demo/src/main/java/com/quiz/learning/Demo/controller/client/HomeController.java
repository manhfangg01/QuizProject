package com.quiz.learning.Demo.controller.client;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HomeController {
    @GetMapping("/")
    public String fetch() {
        return "Welcome home !";
    }
}
