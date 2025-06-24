package com.quiz.learning.Demo.domain.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostSubmissionDTO {
    private long userId;
    private long quizId;
    private List<AnswerDTO> answers;

    @Getter
    @Setter
    public static class AnswerDTO {
        private long questionId;
        private Long selectedOptionId;
    }
}
