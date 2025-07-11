package com.quiz.learning.Demo.domain.response.client;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseClientQuizResultDTO {
    private Long quizId;
    private String quizTitle;
    private int score;
    private int totalQuestions;
    private int totalCorrect;
    private Long duration;
    private Instant submittedAt;
    private List<AnswerReviewDTO> answers;
}
