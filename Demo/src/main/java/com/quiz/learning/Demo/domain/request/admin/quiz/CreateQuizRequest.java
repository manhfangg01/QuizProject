package com.quiz.learning.Demo.domain.request.admin.quiz;

import java.util.List;

import com.quiz.learning.Demo.util.constant.DifficultyLevel;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateQuizRequest {
    @NotBlank(message = "Title can't be blank")
    private String title;

    @NotBlank(message = "Subject name can't be blank")
    private String subjectName;

    @NotNull(message = "Time limit can't be null")
    @Min(value = 1, message = "Time limit must be at least 1 minute")
    private Long timeLimit;

    @NotNull(message = "isActive must not be null")
    private Boolean isActive;

    @NotNull(message = "Difficulty must not be null")
    private DifficultyLevel difficulty;

    @NotNull(message = "Questions list must not be null")
    @Size(min = 1, message = "There must be at least 1 question")
    private List<@NotNull(message = "Question ID cannot be null") Long> questions;
}
