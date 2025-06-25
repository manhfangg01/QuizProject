package com.quiz.learning.Demo.domain.response.admin;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quiz.learning.Demo.util.constant.DifficultyLevel;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

public class FetchAdminDTO {

    @Getter
    @Setter
    public static class FetchQuizDTO {
        private Long quizId;
        private String title;
        private String subjectName;
        private Long timeLimit;
        private Long totalParticipants;
        private boolean isActive;
        @Enumerated(EnumType.STRING)
        private DifficultyLevel difficulty;
        private List<FetchQuestionDTO> questions;
        private List<FetchResultDTO> results; // ✅ dùng DTO thay vì entity
    }

    @Getter
    @Setter
    public static class FetchQuestionDTO {
        private Long questionId;
        private String context;
        private List<FetchOptionDTO> options;
    }

    @Getter
    @Setter
    public static class FetchAnswerDTO {
        private Long id;
        private Long optionId;
        private boolean isCorrect;
    }

    @Getter
    @Setter
    public static class FetchOptionDTO {
        private Long id;
        private String context;
        private List<FetchAnswerDTO> answers; // ✅ dùng DTO thay vì entity

    }

    @Getter
    @Setter
    public static class FetchResultDTO {
        private Long id;
        private Long userId;
        private Long quizId;
        private List<FetchAnswerDTO> answers;
        private int score;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
        private Instant submittedAt;
    }

    @Getter
    @Setter
    public static class FetchUserDTO {
        private Long id;
        private String fullName;
        private String email;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
        private Instant createdAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
        private Instant updatedAt;

        private String createdBy;
        private String updatedBy;

        private Set<String> roles; // chỉ trả về tên vai trò
    }

}
