package com.quiz.learning.Demo.service.specification;

import java.time.Instant;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Quiz_;
import com.quiz.learning.Demo.domain.Result;
import com.quiz.learning.Demo.domain.Result_;
import com.quiz.learning.Demo.domain.User_;

@Service
public class ResultSpecs {
    public Specification<Result> hasId(Long id) {
        return (root, query, cb) -> cb.equal(root.get(Result_.ID), id);
    }

    public Specification<Result> hasUserId(Long userId) {
        return (root, query, cb) -> cb.equal(root.get(Result_.user).get(User_.id), userId);
    }

    public Specification<Result> hasQuizId(Long quizId) {
        return (root, query, cb) -> cb.equal(root.get(Result_.quiz).get(Quiz_.id), quizId);
    }

    public Specification<Result> betweenDates(Instant from, Instant to) {
        return (root, query, cb) -> cb.between(root.get(Result_.SUBMITTED_AT), from, to);
    }

    public Specification<Result> hasScoreGreaterThanOrEqual(Integer score) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(Result_.SCORE), score);

    }

    public Specification<Result> hasCorrectAnswerGreaterThan(Integer totalCorrects) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(Result_.TOTAL_CORRECTED_ANSWER), totalCorrects);
    }

    public Specification<Result> hasDurationLessThan(Long duration) {
        return (root, query, cb) -> cb.lessThan(root.get(Result_.DURATION), duration);

    }

}
