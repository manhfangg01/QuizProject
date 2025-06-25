package com.quiz.learning.Demo.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.learning.Demo.service.admin.AdminUserService;

@RestController
@RequestMapping("/api/v1")
public class AdminUserController {
    private final AdminUserService userService;

    public AdminUserController(AdminUserService userService) {
        this.userService = userService;
    }

}
