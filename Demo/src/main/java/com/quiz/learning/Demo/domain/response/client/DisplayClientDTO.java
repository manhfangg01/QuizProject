package com.quiz.learning.Demo.domain.response.client;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class DisplayClientDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuizPlayDTO {
        private long quizId;
        private String title;
        private long timeLimit;
        private List<QuestionDTO> questions;

        // getters & setters
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionDTO {
        private long questionId;
        private String content;
        private List<OptionDTO> options;

        // getters & setters
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptionDTO {
        private long optionId;
        private String content;

        // getters & setters
    }

}
