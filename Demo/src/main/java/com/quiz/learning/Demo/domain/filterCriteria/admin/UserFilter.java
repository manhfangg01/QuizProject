package com.quiz.learning.Demo.domain.filterCriteria.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFilter {
    private Long id;
    private String fullName;
    private String email;
    private String role;
}
