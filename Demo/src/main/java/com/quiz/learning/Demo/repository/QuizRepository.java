package com.quiz.learning.Demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quiz.learning.Demo.domain.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    public Optional<Quiz> findByTitle(String title);

}
