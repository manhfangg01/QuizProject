package com.quiz.learning.Demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.quiz.learning.Demo.service.ResultService;

@RestController
public class ResultController {
    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

}
