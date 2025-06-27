package com.quiz.learning.Demo.domain.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.quiz.learning.Demo.domain.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    @JsonProperty
    private String accessToken;
    @JsonIgnore
    private UserLogin user;

    @Getter
    @Setter
    public static class UserLogin {
        private Long userId;
        private String fullName;
        private String email;
        private Role role;
    }
}
