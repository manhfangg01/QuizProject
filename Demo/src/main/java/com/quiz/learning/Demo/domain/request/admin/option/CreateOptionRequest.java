package com.quiz.learning.Demo.domain.request.admin.option;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOptionRequest {
    @NotBlank(message = "Context can't be blanked")
    private String context;
    @NotBlank(message = "isCorrect can't be blanked")
    private Boolean isCorrect;
}
