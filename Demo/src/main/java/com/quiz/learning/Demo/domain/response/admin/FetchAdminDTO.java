package com.quiz.learning.Demo.domain.response.admin;

import java.time.Instant;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.quiz.learning.Demo.domain.metadata.Metadata;
import com.quiz.learning.Demo.util.constant.DifficultyLevel;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class FetchAdminDTO {

    @Getter
    @Setter
    public static class AdminStats {
        private Long totalUsers;
        private Long totalQuizzes;
        private Long totalQuestions;
        private Long totalResults;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserTopScoreDTO {
        private String fullName;
        private Double averageScore;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerAccuracyDTO {
        private int correct;
        private int incorrect;
        private int skipped;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class QuizPopularityDTO {
        private String title;
        private Long count;

        // Constructor phải khớp với @Query
        public QuizPopularityDTO(String title, Long count) {
            this.title = title;
            this.count = count;
        }
    }

    @Getter
    @Setter
    public static class FetchQuestionDTO {
        private Long questionId;
        private String context;
        private String questionImage;
        private List<FetchOptionDTO> options;
    }

    @Getter
    @Setter
    public static class FetchAnswerDTO {
        private Long id;
        private Long optionId;
        private Boolean isCorrect;
    }

    @Getter
    @Setter
    public static class FetchOptionDTO {
        private Long id;
        private String context;
        private Boolean isCorrect;
        private Long questionId;

    }

    @Getter
    @Setter
    public static class FetchResultDTO {
        private Long id;
        private Long userId;
        private String userName;
        private Long quizId;
        private String quizTilte;
        private List<FetchAnswerDTO> answers;
        private int totalQuestions;
        private int totalCorrectedAnswer;
        private Long duration;
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
        private String role; // chỉ trả về tên vai trò
        private String UserAvatarUrls;
    }

    @Getter
    @Setter
    public static class FetchUserPaginationDTO {
        private List<FetchUserDTO> users;
        private Metadata metadata;
    }

    @Getter
    @Setter
    public static class FetchFullQuizDTO {
        private Long quizId;
        private String title;
        private String subjectName;
        private Long timeLimit;
        private Long totalParticipants;
        private Boolean isActive;
        @Enumerated(EnumType.STRING)
        private DifficultyLevel difficulty;
        private List<FetchQuestionDTO> questions;
        private List<FetchResultDTO> results; // ✅ dùng DTO thay vì entity
    }

    @Getter
    @Setter
    public static class FetchTableQuizDTO {
        private Long quizId;
        private String title;
        private String subjectName;
        private Long timeLimit;
        private Long totalParticipants;
        private Boolean isActive;
        @Enumerated(EnumType.STRING)
        private DifficultyLevel difficulty;
    }

    @Getter
    @Setter
    public static class FetchQuizPaginationDTO {
        private List<FetchFullQuizDTO> quizzes;
        private Metadata metadata;
    }

    @Getter
    @Setter
    public static class FetchOptionPaginationDTO {
        private List<FetchOptionDTO> options;
        private Metadata metadata;
    }

    @Getter
    @Setter
    public static class FetchQuestionPaginationDTO {
        private List<FetchQuestionDTO> questions;
        private Metadata metadata;
    }

    @Getter
    @Setter
    public static class FetchResultPaginationDTO {
        private List<FetchResultDTO> results;
        private Metadata metadata;
    }

}
