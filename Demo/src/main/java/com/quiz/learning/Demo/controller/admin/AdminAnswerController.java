package com.quiz.learning.Demo.controller.admin;

import org.springframework.web.bind.annotation.RestController;

import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.AnswerAccuracyDTO;
import com.quiz.learning.Demo.service.admin.AdminAnswerService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api")
public class AdminAnswerController {
    private final AdminAnswerService adminAnswerService;

    public AdminAnswerController(AdminAnswerService adminAnswerService) {
        this.adminAnswerService = adminAnswerService;
    }

    @GetMapping("/admin/answers/fetch-accuracy")
    public ResponseEntity<AnswerAccuracyDTO> getAccuracy() {
        return ResponseEntity.ok(this.adminAnswerService.handleFetchAccuracy());

    }

}
