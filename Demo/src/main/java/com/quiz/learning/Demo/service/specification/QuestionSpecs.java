package com.quiz.learning.Demo.service.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.quiz.learning.Demo.domain.Option_;
import com.quiz.learning.Demo.domain.Question;
import com.quiz.learning.Demo.domain.Question_;
import com.quiz.learning.Demo.domain.Quiz_;

@Service
public class QuestionSpecs {
    public Specification<Question> hasId(Long id) {
        return (root, query, cb) -> {
            if (id != null) {
                return cb.equal(root.get(Question_.ID), id);
            }
            return null;
        };
    }

    public Specification<Question> contextContains(String keyword) {
        return (root, query, cb) -> {
            if (StringUtils.hasText(keyword)) {
                return cb.like(cb.lower(root.get(Question_.CONTEXT)), "%" + keyword.toLowerCase() + "%");
            }
            return null;
        };
    }

    public Specification<Question> hasAtLeastNOptions(Integer n) {
        return (root, query, cb) -> {
            if (n != null) {
                root.join(Question_.OPTIONS);
                query.groupBy(root.get(Question_.ID)); // sửa lại đúng field ID
                query.having(cb.greaterThanOrEqualTo(cb.count(root.get(Question_.OPTIONS)), cb.literal(n.longValue())));
            }
            return cb.conjunction(); // luôn hợp lệ, dùng để không lỗi syntax
        };
    }

    public Specification<Question> hasQuizId(Long quizId) {
        return (root, query, cb) -> {
            if (quizId != null) {
                return cb.equal(root.join(Question_.QUIZZES).get(Quiz_.ID), quizId);
            }
            return null;
        };
    }

    // Cái belongsToQuiz có thể bỏ nếu dùng hasQuizId rồi
}
