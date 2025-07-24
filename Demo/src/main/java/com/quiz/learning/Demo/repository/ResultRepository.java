package com.quiz.learning.Demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.quiz.learning.Demo.domain.Quiz;
import com.quiz.learning.Demo.domain.Result;
import com.quiz.learning.Demo.domain.User;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long>, JpaSpecificationExecutor<Result> {
    public List<Result> findByQuizId(Long quizId);

    public Optional<Result> findByUserAndQuiz(User user, Quiz quiz);

    public List<Result> findAllByUser(User user);

    public Page<Result> findAllByUser(User user, Pageable pageable);

    public List<Result> findAllByUserIdAndQuizId(Long userId, Long quizId);

}
