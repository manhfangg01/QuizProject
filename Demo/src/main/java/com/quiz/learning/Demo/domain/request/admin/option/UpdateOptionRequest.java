package com.quiz.learning.Demo.domain.request.admin.option;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOptionRequest {
    private long id;
    private String context;
    private boolean isCorrect;
}
