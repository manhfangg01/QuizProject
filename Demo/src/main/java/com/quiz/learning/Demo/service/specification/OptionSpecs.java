package com.quiz.learning.Demo.service.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Option;
import com.quiz.learning.Demo.domain.Option_;
import com.quiz.learning.Demo.domain.Question_;

@Service
public class OptionSpecs {
    public Specification<Option> hasId(Long id) {
        return (root, query, cb) -> cb.equal(cb.lower(root.get(Option_.ID)), id);

    }

    public Specification<Option> contextContains(String keyword) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(Option_.CONTEXT)), "%" + keyword.toLowerCase() + "%");
    }

    public Specification<Option> isCorrect() {
        return (root, query, cb) -> cb.isTrue(root.get(Option_.IS_CORRECT));
    }

    public Specification<Option> hasQuestionId(Long questionId) {
        return (root, query, cb) -> cb.equal(root.get(Option_.QUESTION).get(Question_.ID), questionId);
    }

}
