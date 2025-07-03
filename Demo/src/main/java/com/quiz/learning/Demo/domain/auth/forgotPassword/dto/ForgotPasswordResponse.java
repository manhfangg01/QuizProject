package com.quiz.learning.Demo.domain.auth.forgotPassword.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class ForgotPasswordResponse {
    @Setter
    @Getter
    public static class EmailCheckingResponse {
        private String message;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class ResetPasswordResponse {
        private String message;
    }

}
