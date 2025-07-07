package com.quiz.learning.Demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.quiz.learning.Demo.domain.Option;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long>, JpaSpecificationExecutor<Option> {
    public Optional<Option> findByContext(String context);

}
