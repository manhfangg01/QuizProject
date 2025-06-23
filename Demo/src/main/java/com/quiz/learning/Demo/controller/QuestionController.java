package com.quiz.learning.Demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.quiz.learning.Demo.service.QuestionService;

@RestController
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

}
