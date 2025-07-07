package com.quiz.learning.Demo.service.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.quiz.learning.Demo.domain.User;
import com.quiz.learning.Demo.domain.User_;

@Service
public class UserSpecs {
    public Specification<User> hasId(Long id) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (id != null) {
                return criteriaBuilder.equal(root.get(User_.ID), id);
            }
            return null;
        };
    }

    public Specification<User> nameLike(String name) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (StringUtils.hasText(name)) {
                return criteriaBuilder.like(root.get(User_.FULL_NAME), "%" + name + "%");
            }
            return null;
        };
    }

    public Specification<User> hasEmailLike(String email) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (StringUtils.hasText(email)) {
                return criteriaBuilder.like(root.get(User_.EMAIL), "%" + email + "%");
            }
            return null;
        };
    }

    public Specification<User> hasRole(String role) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (StringUtils.hasText(role)) {
                return criteriaBuilder.equal(root.get(User_.ROLE).get("name"), role);
            }
            return null;
        };
    }

}
