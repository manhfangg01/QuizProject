package com.quiz.learning.Demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Answer;
import com.quiz.learning.Demo.repository.AnswerRepository;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;

    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public List<Answer> handleFetchAllAnswer() {
        return this.answerRepository.findAll();
    }

}
