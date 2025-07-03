package com.quiz.learning.Demo.domain.auth.forgotPassword.dto;

import lombok.Getter;
import lombok.Setter;

public class ForgotPasswordRequest {
    @Getter
    @Setter
    public static class EmailCheckingRequest {
        private String email;
    }

    @Getter
    @Setter
    public static class ResetPasswordRequest {
        private String resetToken;
        private String newPassword;
    }

}
