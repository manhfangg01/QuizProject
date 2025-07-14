package com.quiz.learning.Demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.quiz.learning.Demo.domain.Quiz;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.QuizPopularityDTO;

public interface QuizRepository extends JpaRepository<Quiz, Long>, JpaSpecificationExecutor<Quiz> {
    public Optional<Quiz> findByTitle(String title);

    @Query("SELECT new com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO$QuizPopularityDTO(q.title, COUNT(r.id)) "
            +
            "FROM Result r JOIN r.quiz q " +
            "GROUP BY q.id, q.title " +
            "ORDER BY COUNT(r.id) DESC")
    List<QuizPopularityDTO> findTopQuizzes(Pageable pageable);

}
