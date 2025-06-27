package com.quiz.learning.Demo.domain.request.admin.question;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateQuestionRequest {
    private Long questionId;
    private String context;
    private List<Long> options;
}
