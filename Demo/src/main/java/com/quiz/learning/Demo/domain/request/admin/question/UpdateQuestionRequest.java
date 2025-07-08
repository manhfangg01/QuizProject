package com.quiz.learning.Demo.domain.request.admin.question;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateQuestionRequest {
    @NotNull(message = "Id can't be null")
    private Long questionId;
    @NotBlank(message = "Context can't be blanked")
    private String context;
    @NotNull(message = "Options list must not be null")
    @Size(min = 4, max = 4, message = "Options list must contain exactly 4 items")
    private List<Long> optionIds;
}
