package com.quiz.learning.Demo.domain.filterCriteria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionFilter {
    private Long id;
    private String context;
    private Integer numberOfOptions;
    private Long quizId;

}
