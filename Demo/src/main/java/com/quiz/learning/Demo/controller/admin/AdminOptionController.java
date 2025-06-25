package com.quiz.learning.Demo.controller.admin;

import org.springframework.web.bind.annotation.RestController;

import com.quiz.learning.Demo.service.admin.AdminOptionService;

@RestController
public class AdminOptionController {
    private final AdminOptionService optionService;

    public AdminOptionController(AdminOptionService optionService) {
        this.optionService = optionService;
    }

}
