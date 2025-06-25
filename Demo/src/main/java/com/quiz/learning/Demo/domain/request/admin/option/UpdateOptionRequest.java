package com.quiz.learning.Demo.domain.request.admin.option;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOptionRequest {
    private Long id;
    private String context;
    private boolean isCorrect;
}
