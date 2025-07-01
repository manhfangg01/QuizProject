package com.quiz.learning.Demo.domain.request.admin.quiz;

import java.util.List;

import com.quiz.learning.Demo.util.constant.DifficultyLevel;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateQuizRequest {

    @NotNull(message = "Quiz ID must not be null")
    private Long quizId; // ID của quiz cần cập nhật

    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title; // Tên mới (nếu có)

    @Size(min = 2, max = 50, message = "Subject name must be between 2 and 50 characters")
    private String subjectName; // Môn học

    @Min(value = 1, message = "Time limit must be at least 1 minute")
    private Long timeLimit; // Giới hạn thời gian

    private Boolean isActive; // Trạng thái (mở/bị ẩn)

    private DifficultyLevel difficulty; // Enum độ khó

    @Size(min = 1, message = "There must be at least 1 question")
    private List<@NotNull(message = "Question ID cannot be null") Long> questions;
}