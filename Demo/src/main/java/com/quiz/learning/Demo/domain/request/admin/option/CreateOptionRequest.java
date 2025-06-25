package com.quiz.learning.Demo.domain.request.admin.option;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOptionRequest {
    private String context;
    private boolean isCorrect;
}
