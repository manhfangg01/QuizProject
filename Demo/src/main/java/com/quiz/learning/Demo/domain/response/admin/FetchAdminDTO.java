package com.quiz.learning.Demo.domain.response.admin;

import java.time.Instant;
import java.util.List;

import com.quiz.learning.Demo.util.constant.DifficultyLevel;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

public class FetchAdminDTO {

    @Getter
    @Setter
    public static class FetchQuizDTO {
        private long quizId;
        private String title;
        private String subjectName;
        private long timeLimit;
        private long totalParticipants;
        private boolean isActive;
        @Enumerated(EnumType.STRING)
        private DifficultyLevel difficulty;
        private List<FetchQuestionDTO> questions;
        private List<FetchResultDTO> results; // ✅ dùng DTO thay vì entity
    }

    @Getter
    @Setter
    public static class FetchQuestionDTO {
        private long questionId;
        private String context;
        private List<FetchAnswerDTO> answers; // ✅ dùng DTO thay vì entity
        private List<FetchOptionDTO> options;
    }

    @Getter
    @Setter
    public static class FetchAnswerDTO {
        private long id;
        private long optionId;
        private boolean isCorrect;
    }

    @Getter
    @Setter
    public static class FetchOptionDTO {
        private long id;
        private String content;
    }

    @Getter
    @Setter
    public static class FetchResultDTO {
        private long id;
        private long userId;
        private int score;
        private Instant submittedAt;
    }

}
