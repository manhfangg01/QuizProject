package com.quiz.learning.Demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.quiz.learning.Demo.domain.Quiz;
import com.quiz.learning.Demo.domain.QuizProgress;
import com.quiz.learning.Demo.domain.User;

@Repository
public interface QuizProgressRepository extends JpaRepository<QuizProgress, Long> {
    public Optional<QuizProgress> findByUserAndQuiz(User user, Quiz quiz);

    @Modifying
    @Transactional
    @Query("DELETE FROM QuizProgress qp WHERE qp.user.id = :userId AND qp.quiz.id = :quizId")
    void deleteAllByUserIdAndQuizId(@Param("userId") Long userId, @Param("quizId") Long quizId);
}
