package com.quiz.learning.Demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quiz.learning.Demo.domain.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    public Optional<Question> findByContext(String context);
}
