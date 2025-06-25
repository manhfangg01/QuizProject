package com.quiz.learning.Demo.controller.admin;

import org.springframework.web.bind.annotation.RestController;

import com.quiz.learning.Demo.service.admin.AdminResultService;

@RestController
public class AdminResultController {
    private final AdminResultService resultService;

    public AdminResultController(AdminResultService resultService) {
        this.resultService = resultService;
    }

}
