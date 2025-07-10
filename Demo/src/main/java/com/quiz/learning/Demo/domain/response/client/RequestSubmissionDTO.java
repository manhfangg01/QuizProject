package com.quiz.learning.Demo.domain.response.client;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestSubmissionDTO {
    private Long quizId;
    private Long userId;
    private Long duration; // tính bằng giây

    private List<SubmittedAnswer> answers;

    @Getter
    @Setter
    public static class SubmittedAnswer {
        private Long questionId;
        private Long selectedOptionId;
    }

}
