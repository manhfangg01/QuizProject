package com.quiz.learning.Demo.service.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.quiz.learning.Demo.domain.Quiz;
import com.quiz.learning.Demo.domain.Quiz_;
import com.quiz.learning.Demo.util.constant.DifficultyLevel;

@Service
public class QuizSpecs {

    public Specification<Quiz> hasId(Long id) {
        if (id == null)
            return null;
        return (root, query, cb) -> cb.equal(root.get(Quiz_.ID), id);
    }

    public Specification<Quiz> titleContains(String keyword) {
        if (!StringUtils.hasText(keyword))
            return null;
        return (root, query, cb) -> cb.like(cb.lower(root.get(Quiz_.TITLE)), "%" + keyword.toLowerCase() + "%");
    }

    public Specification<Quiz> hasSubject(String subjectName) {
        if (!StringUtils.hasText(subjectName))
            return null;
        return (root, query, cb) -> cb.equal(cb.lower(root.get(Quiz_.SUBJECT_NAME)), subjectName.toLowerCase());
    }

    public Specification<Quiz> hasDifficulty(DifficultyLevel level) {
        if (level == null)
            return null;
        return (root, query, cb) -> cb.equal(root.get(Quiz_.DIFFICULTY), level);
    }

    public Specification<Quiz> isActive(Boolean active) {
        if (active == null)
            return null;
        return (root, query, cb) -> cb.equal(root.get(Quiz_.IS_ACTIVE), active);
    }

    public Specification<Quiz> hasMinParticipants(Long minParticipants) {
        if (minParticipants == null)
            return null;
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(Quiz_.TOTAL_PARTICIPANTS), minParticipants);
    }

    public Specification<Quiz> timeLimitLessThanOrEqual(Long maxTimeInMinutes) {
        if (maxTimeInMinutes == null)
            return null;
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(Quiz_.TIME_LIMIT), maxTimeInMinutes);
    }
}
