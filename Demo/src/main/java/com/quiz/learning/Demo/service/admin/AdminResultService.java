package com.quiz.learning.Demo.service.admin;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.repository.ResultRepository;

@Service
public class AdminResultService {
    private final ResultRepository resultRepository;

    public AdminResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

}
