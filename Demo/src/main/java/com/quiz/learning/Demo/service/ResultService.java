package com.quiz.learning.Demo.service;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.repository.ResultRepository;

@Service
public class ResultService {
    private final ResultRepository resultRepository;

    public ResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

}
