package com.quiz.learning.Demo.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "quiz_progress")
@Getter
@Setter
public class QuizProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Thời gian đã làm bài (tính bằng giây)
    private Long duration;

    // Dạng JSON chứa danh sách câu trả lời tạm thời
    @Column(columnDefinition = "TEXT")
    private String answersJson;

    @Column(name = "saved_at")
    private Instant savedAt;

    private Boolean manuallyExited; // Sau khi người dùng quay lại bài làm bạn có thể dùng để liệu người dùng có
                                    // muốn làm bài tiếp

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @PrePersist
    public void prePersist() {
        this.savedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.savedAt = Instant.now();
    }
}