package com.quiz.learning.Demo.domain.request.admin.quiz;

import java.util.List;

import com.quiz.learning.Demo.util.constant.DifficultyLevel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateQuizRequest {
    private Long quizId; // ID của quiz cần cập nhật
    private String title; // Tên mới (nếu có)
    private String subjectName; // Môn học
    private Long timeLimit; // Giới hạn thời gian
    private Boolean isActive; // Trạng thái (mở/bị ẩn)
    private DifficultyLevel difficulty; // Độ khó (Easy, Medium, Hard - bạn có thể dùng Enum)
    private List<Long> questions; // Danh sách các ID câu hỏi
}