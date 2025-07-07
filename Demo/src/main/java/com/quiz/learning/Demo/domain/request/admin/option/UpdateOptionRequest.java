package com.quiz.learning.Demo.domain.request.admin.option;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOptionRequest {
    @NotNull(message = "Id can't be null")
    private Long optionId;
    @NotBlank(message = "Context can't be blanked")
    private String context;
    private Boolean isCorrect;
}
