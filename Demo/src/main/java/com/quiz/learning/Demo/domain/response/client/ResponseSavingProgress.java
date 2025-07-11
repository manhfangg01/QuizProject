package com.quiz.learning.Demo.domain.response.client;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseSavingProgress {
    private Long quizId;
    private Long userId;
    private Instant savedAt;

}
