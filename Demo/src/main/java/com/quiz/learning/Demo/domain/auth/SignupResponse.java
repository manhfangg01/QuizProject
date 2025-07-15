package com.quiz.learning.Demo.domain.auth;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupResponse {
    private Long userId;
    private String email;
    private String fullName;
    private Instant createdAt;
    private String role;

}
