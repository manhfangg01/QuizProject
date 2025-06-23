package com.quiz.learning.Demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.quiz.learning.Demo.service.AnswerService;

@RestController
public class AnswerController {
    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

}
