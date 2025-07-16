package com.quiz.learning.Demo.domain.response.client;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseSubmissionDTO {
    private Long quizId;
    private Long userId;
    private Long resultId;
    private int score;
    private int totalQuestions;
    private int totalCorrectedAnswer;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Instant submittedAt;
    private List<Detail> details;
    private Long duration;

    @Getter
    @Setter
    public static class Detail {
        private Long questionId;
        private Long selectedOptionId;
        private Long correctOptionId;
        private Boolean isCorrect;
    }

}
