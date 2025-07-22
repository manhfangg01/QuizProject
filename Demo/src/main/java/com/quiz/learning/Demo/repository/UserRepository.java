package com.quiz.learning.Demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.quiz.learning.Demo.domain.User;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.UserTopScoreDTO;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    public Optional<User> findByEmail(String email);

    public Optional<User> findByFullName(String fullName);

    @Query("SELECT new com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO$UserTopScoreDTO(u.fullName, AVG(r.score)) "
            +
            "FROM Result r JOIN r.user u " +
            "GROUP BY u.id, u.fullName " +
            "ORDER BY AVG(r.score) DESC")
    List<UserTopScoreDTO> findTopUsersByAverageScore(Pageable pageable);

    public Optional<User> findByFirebaseUid(String firebaseUid);
}
