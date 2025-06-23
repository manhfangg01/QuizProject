package com.quiz.learning.Demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<Answer> handleFetchAllCorrectedAnswers() {
        return this.answerRepository.findAll().stream().filter(answer -> answer.isCorrect())
                .collect(Collectors.toList());
    }

    public Optional<Answer> handleFetchAnswerById(long id) {
        return this.answerRepository.findById(id);
    }

}
