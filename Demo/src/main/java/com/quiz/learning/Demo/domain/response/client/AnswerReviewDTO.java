package com.quiz.learning.Demo.domain.response.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerReviewDTO {
    private Long questionId;
    private String questionContent;
    private Long selectedOptionId;
    private String selectedOptionContent;
    private Boolean isCorrect;
    private Long correctOptionId;
}
