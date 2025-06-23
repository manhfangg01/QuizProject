package com.quiz.learning.Demo.service;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.repository.QuizRepository;

@Service
public class QuizService {
    private final QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

}
