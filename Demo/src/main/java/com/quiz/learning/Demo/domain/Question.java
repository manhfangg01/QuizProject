package com.quiz.learning.Demo.domain;

import java.time.Instant;
import java.util.List;

import com.quiz.learning.Demo.util.constant.AnswerOption;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String context;

    @OneToMany(mappedBy = "question")
    private List<Option> options;

    @ManyToMany(mappedBy = "questions")
    private List<Quiz> quizzies;

}
