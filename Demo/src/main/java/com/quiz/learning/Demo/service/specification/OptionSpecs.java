package com.quiz.learning.Demo.service.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.quiz.learning.Demo.domain.Option;
import com.quiz.learning.Demo.domain.Option_;
import com.quiz.learning.Demo.domain.Question_;

@Service
public class OptionSpecs {

    public Specification<Option> hasId(Long id) {
        return (root, query, cb) -> {
            if (id != null) {
                return cb.equal(root.get(Option_.ID), id);
            }
            return null;
        };
    }

    public Specification<Option> contextContains(String keyword) {
        return (root, query, cb) -> {
            if (StringUtils.hasText(keyword)) {
                return cb.like(cb.lower(root.get(Option_.CONTEXT)), "%" + keyword.toLowerCase() + "%");
            }
            return null;
        };
    }

    public Specification<Option> isCorrect(Boolean isCorrect) {
        return (root, query, cb) -> {
            if (isCorrect != null) {
                return cb.equal(root.get(Option_.IS_CORRECT), isCorrect);
            }
            return null;
        };
    }

    public Specification<Option> hasQuestionId(Long questionId) {
        return (root, query, cb) -> {
            if (questionId != null) {
                return cb.equal(root.get(Option_.QUESTION).get(Question_.ID), questionId);
            }
            return null;
        };
    }
}
