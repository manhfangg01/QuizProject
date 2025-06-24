package com.quiz.learning.Demo.domain;

import java.util.List;
import com.quiz.learning.Demo.util.constant.DifficultyLevel;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "quiz")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String subjectName;
    private long timeLimit;
    private long totalParticipants;
    private boolean isActive;
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficulty;

    @OneToMany(mappedBy = "quiz")
    private List<Result> results;

    @OneToMany(mappedBy = "quiz")
    private List<Question> questions;

}
