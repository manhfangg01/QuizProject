package com.quiz.learning.Demo.domain.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "username không được bỏ trống")
    private String username;
    @NotBlank(message = "password không được bỏ trống")
    private String password;

}
