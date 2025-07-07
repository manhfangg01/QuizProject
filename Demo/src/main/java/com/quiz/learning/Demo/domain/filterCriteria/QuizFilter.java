package com.quiz.learning.Demo.domain.filterCriteria;

import com.quiz.learning.Demo.util.constant.DifficultyLevel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizFilter {
    private Long id;
    private String title;
    private String subject;
    private DifficultyLevel difficulty;
    private Boolean active;
    private Long totalParticipants;
    private Long timeLimit;

}
