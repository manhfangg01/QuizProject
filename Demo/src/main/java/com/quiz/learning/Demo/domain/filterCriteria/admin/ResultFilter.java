package com.quiz.learning.Demo.domain.filterCriteria.admin;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultFilter {
    private Long id;
    private Long userId;
    private Long quizId;
    private Instant from;
    private Instant to;
    private Integer score;
    private Integer totalCorrects;
    private Long duration;
}