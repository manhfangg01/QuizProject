package com.quiz.learning.Demo.domain.response.client;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseSubmissionDTO {
    private long quizId;
    private long userId;
    private int score;
    private int totalQuestions;
    private Instant submittedAt;
    private List<Detail> details;

    @Getter
    @Setter
    public static class Detail {
        private long questionId;
        private Long selectedOptionId;
        private boolean isCorrect;
    }

}
