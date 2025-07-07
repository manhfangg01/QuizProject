package com.quiz.learning.Demo.service.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Option_;
import com.quiz.learning.Demo.domain.Question;
import com.quiz.learning.Demo.domain.Question_;
import com.quiz.learning.Demo.domain.Quiz_;

@Service
public class QuestionSpecs {
    public Specification<Question> hasId(Long id) {
        return (root, query, cb) -> cb.equal(root.get(Question_.ID), id);

    }

    public Specification<Question> contextContains(String keyword) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(Question_.CONTEXT)), "%" + keyword.toLowerCase() + "%");
    }

    public Specification<Question> hasAtLeastNOptions(Integer n) {
        return (root, query, cb) -> {
            // Join đến Option và đếm
            root.join(Question_.OPTIONS);
            query.groupBy(root.get(Option_.ID));
            query.having(cb.greaterThanOrEqualTo(cb.count(root.get(Question_.OPTIONS)), cb.literal(n.longValue())));
            return cb.conjunction(); // điều kiện được đặt qua having
        };
    }

    public Specification<Question> belongsToQuiz(Long quizId) {
        return (root, query, cb) -> {
            return cb.isMember(
                    quizId,
                    root.join(Question_.QUIZZES).get(Quiz_.ID));
        };
    }

    public Specification<Question> hasQuizId(Long quizId) {
        return (root, query, cb) -> cb.equal(root.join(Question_.QUIZZES).get(Quiz_.ID), quizId);
    }

}
