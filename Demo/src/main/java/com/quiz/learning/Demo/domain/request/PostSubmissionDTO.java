package com.quiz.learning.Demo.domain.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostSubmissionDTO {
    private Long userId;
    private Long quizId;
    private List<AnswerDTO> answers;

    @Getter
    @Setter
    public static class AnswerDTO {
        private Long questionId;
        private Long selectedOptionId;
    }
}
