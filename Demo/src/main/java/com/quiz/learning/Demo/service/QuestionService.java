package com.quiz.learning.Demo.service;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.repository.QuestionRepository;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

}
