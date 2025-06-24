package com.quiz.learning.Demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.quiz.learning.Demo.service.QuestionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/admin/questions/fetch")
    public String fetchAllQuestions() {
        return new String();
    }

}
