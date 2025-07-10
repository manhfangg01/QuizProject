package com.quiz.learning.Demo.domain.response.client;

import java.util.List;

import com.quiz.learning.Demo.domain.metadata.Metadata;
import com.quiz.learning.Demo.util.constant.DifficultyLevel;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

public class FetchClientDTO {
    @Getter
    @Setter
    public static class QuizClientDTO {
        private Long quizId;
        private String title;
        @Enumerated(EnumType.STRING)
        private DifficultyLevel difficulty;
    }

    @Getter
    @Setter
    public static class QuizClientPaginationDTO {
        private List<QuizClientDTO> quizzes;
        private Metadata metadata;
    }

    @Getter
    @Setter
    public static class QuizClientPlayDTO {
        private Long quizId;
        private String title;
        private String subjectName;
        @Enumerated(EnumType.STRING)
        private DifficultyLevel difficulty;
        private Long timeLimit;
        private Integer numberOfQuestion;
        private Long totalParticipants;
        private Boolean isActive;
        private List<QuestionClientPlayDTO> questions;
    }

    @Getter
    @Setter
    public static class QuestionClientPlayDTO {
        private Long questionId;
        private String context;
        private List<OptionClientPlayDTO> options;
    }

    @Getter
    @Setter

    public static class OptionClientPlayDTO {
        private Long optionId;
        private String content;
    }

}
