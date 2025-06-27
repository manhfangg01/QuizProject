package com.quiz.learning.Demo.domain.request.admin.question;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateQuestionRequest {
    private String context;
    private List<Long> options;
}
