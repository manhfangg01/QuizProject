package com.quiz.learning.Demo.service.specification;

import java.time.Instant;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Quiz_;
import com.quiz.learning.Demo.domain.Result;
import com.quiz.learning.Demo.domain.Result_;
import com.quiz.learning.Demo.domain.User_;
import com.quiz.learning.Demo.domain.filterCriteria.admin.ResultFilter;

@Service
public class ResultSpecs {
    public Specification<Result> hasId(Long id) {
        return (root, query, cb) -> {
            if (id != null) {
                return cb.equal(root.get(Result_.ID), id);
            }
            return null;
        };
    }

    public Specification<Result> hasUserId(Long userId) {
        return (root, query, cb) -> {
            if (userId != null) {
                return cb.equal(root.get(Result_.user).get(User_.id), userId);
            }
            return null;
        };
    }

    public Specification<Result> hasQuizId(Long quizId) {
        return (root, query, cb) -> {
            if (quizId != null) {
                return cb.equal(root.get(Result_.quiz).get(Quiz_.id), quizId);
            }
            return null;
        };
    }

    public Specification<Result> betweenDates(Instant from, Instant to) {
        return (root, query, cb) -> {
            if (from != null && to != null) {
                return cb.between(root.get(Result_.SUBMITTED_AT), from, to);
            } else if (from != null) {
                return cb.greaterThanOrEqualTo(root.get(Result_.SUBMITTED_AT), from);
            } else if (to != null) {
                return cb.lessThanOrEqualTo(root.get(Result_.SUBMITTED_AT), to);
            }
            return null;
        };
    }

    public Specification<Result> hasScoreGreaterThanOrEqual(Integer score) {
        return (root, query, cb) -> {
            if (score != null) {
                return cb.greaterThanOrEqualTo(root.get(Result_.SCORE), score);
            }
            return null;
        };
    }

    public Specification<Result> hasCorrectAnswerGreaterThan(Integer totalCorrects) {
        return (root, query, cb) -> {
            if (totalCorrects != null) {
                return cb.greaterThanOrEqualTo(root.get(Result_.TOTAL_CORRECTED_ANSWER), totalCorrects);
            }
            return null;
        };
    }

    public Specification<Result> hasDurationLessThan(Long duration) {
        return (root, query, cb) -> {
            if (duration != null) {
                return cb.lessThan(root.get(Result_.DURATION), duration);
            }
            return null;
        };
    }

    public Specification<Result> build(ResultFilter filter) {
        Specification<Result> spec = (root, criteria, cb) -> cb.conjunction();
        Specification<Result> spec1 = this.betweenDates(filter.getFrom(), filter.getTo());
        Specification<Result> spec2 = this.hasCorrectAnswerGreaterThan(filter.getTotalCorrects());
        Specification<Result> spec3 = this.hasDurationLessThan(filter.getDuration());
        Specification<Result> spec4 = this.hasId(filter.getId());
        Specification<Result> spec5 = this.hasQuizId(filter.getQuizId());
        Specification<Result> spec6 = this.hasScoreGreaterThanOrEqual(filter.getScore());
        Specification<Result> spec7 = this.hasUserId(filter.getUserId());
        spec = spec.and(spec1).and(spec2).and(spec3).and(spec4).and(spec5).and(spec6).and(spec7);
        return spec;
    }
}
