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
        private Long quizId;
        private String title;
        private Long timeLimit;
        private List<QuestionDTO> questions;

        // getters & setters
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionDTO {
        private Long questionId;
        private String content;
        private List<OptionDTO> options;

        // getters & setters
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptionDTO {
        private Long optionId;
        private String content;

        // getters & setters
    }

}
