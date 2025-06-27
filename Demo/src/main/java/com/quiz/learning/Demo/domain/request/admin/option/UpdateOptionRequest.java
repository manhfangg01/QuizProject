package com.quiz.learning.Demo.domain.request.admin.option;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOptionRequest {
    private Long optionId;
    private String context;
    private Boolean isCorrect;
}
