package com.quiz.learning.Demo.service.client;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Answer;
import com.quiz.learning.Demo.domain.request.client.SubmittedAnswer;
import com.quiz.learning.Demo.repository.AnswerRepository;

@Service
public class ClientAnswerService {
    private final AnswerRepository answerRepository;

    public ClientAnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public SubmittedAnswer convertToAnswer(Answer answer) {
        if (answer == null)
            return null;
        SubmittedAnswer submittedAnswer = new SubmittedAnswer();
        if (answer.getSelectedOption() != null) {
            submittedAnswer.setQuestionId(answer.getSelectedOption().getQuestion() == null ? null
                    : answer.getSelectedOption().getQuestion().getId());
            submittedAnswer.setSelectedOptionId(answer.getSelectedOption().getId());
        }
        return submittedAnswer;
    }
}
