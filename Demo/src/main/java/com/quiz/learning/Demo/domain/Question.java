package com.quiz.learning.Demo.domain;

import java.util.List;

import com.quiz.learning.Demo.util.constant.QuestionSubType;
import com.quiz.learning.Demo.util.constant.QuestionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "questions")
@Getter
@Setter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String context;
    private String explaination;
    private String questionImage;
    private String questionAudio;
    @Enumerated(EnumType.STRING)
    private QuestionType type;
    @Enumerated(EnumType.STRING)
    private QuestionSubType subType;

    @OneToMany(mappedBy = "question")
    private List<Option> options;

    @ManyToMany(mappedBy = "questions")
    private List<Quiz> quizzes;

}
