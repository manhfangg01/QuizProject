package com.quiz.learning.Demo.domain.request.admin.quiz;

import java.util.List;

import com.quiz.learning.Demo.util.constant.DifficultyLevel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateQuizRequest {
    private String title;
    private String subjectName;
    private Long timeLimit;
    private Boolean isActive;
    private DifficultyLevel difficulty;
    private List<Long> questions;
}
