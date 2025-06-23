package com.quiz.learning.Demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quiz.learning.Demo.domain.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

}
