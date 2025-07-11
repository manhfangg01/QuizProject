package com.quiz.learning.Demo.domain.response.client;

import java.time.Instant;

import com.quiz.learning.Demo.util.constant.DifficultyLevel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientResultHistoryDTO {
    private Long quizId;
    private String quizTitle;
    private Instant submittedAt;
    private int score;
    private int totalQuestions;
    private int totalCorrect;
    private Long duration;
    private DifficultyLevel difficulty;
}