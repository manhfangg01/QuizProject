package com.quiz.learning.Demo.domain.request.client;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestExitingQuiz {
    private Long quizId;
    private Long userId;
    private Long duration; // tính bằng giây

    private List<SubmittedAnswer> answers;

}
