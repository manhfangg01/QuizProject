package com.quiz.learning.Demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quiz.learning.Demo.domain.Option;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

}
