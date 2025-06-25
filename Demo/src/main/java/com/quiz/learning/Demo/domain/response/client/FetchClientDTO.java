package com.quiz.learning.Demo.domain.response.client;

import com.quiz.learning.Demo.util.constant.DifficultyLevel;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class FetchClientDTO {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuizClientDTO {
        private Long id;
        private String title;
        private String subjectName;
        @Enumerated(EnumType.STRING)
        private DifficultyLevel difficulty;
        private Long timeLimit;
        private int numberOfQuestion;
        private Boolean isActive;
    }

}
