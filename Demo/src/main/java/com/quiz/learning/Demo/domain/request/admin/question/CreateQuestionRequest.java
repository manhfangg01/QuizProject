package com.quiz.learning.Demo.domain.request.admin.question;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class CreateQuestionRequest {
    @NotBlank(message = "Context can't be blanked")
    private String context;
    @NotNull(message = "Options list can't be null")
    @Size(min = 1, max = 4, message = "Options list must contain at least 1 and not more than 4")
    private List<Long> optionIds;
}
