package com.quiz.learning.Demo.domain.filterCriteria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptionFilter {
    private Long id;
    private String context;
    private Boolean isCorrect;
    private Long questionId;

}
