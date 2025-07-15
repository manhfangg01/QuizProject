package com.quiz.learning.Demo.domain.filterCriteria.client;

import com.quiz.learning.Demo.util.constant.DifficultyLevel;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizClientFilter {
    private String title;
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficulty;
    private String subject;
    private Long timeLimit;
}
