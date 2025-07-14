package com.quiz.learning.Demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.quiz.learning.Demo.domain.Answer;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.AnswerAccuracyDTO;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    public void deleteAllByResultId(Long resultId);

    @Query("SELECT " +
            "SUM(CASE WHEN a.isCorrect = true THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN a.isCorrect = false THEN 1 ELSE 0 END) " +
            "FROM Answer a")
    public AnswerAccuracyDTO getAnswerAccuracyStats();

}
