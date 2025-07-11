package com.quiz.learning.Demo.domain.request.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmittedAnswer {
    private Long questionId;
    private Long selectedOptionId;
}