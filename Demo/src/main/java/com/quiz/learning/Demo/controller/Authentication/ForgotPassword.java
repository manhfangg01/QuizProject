package com.quiz.learning.Demo.controller.authentication;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.learning.Demo.domain.auth.forgotPassword.dto.ForgotPasswordRequest.EmailCheckingRequest;
import com.quiz.learning.Demo.domain.auth.forgotPassword.dto.ForgotPasswordRequest.ResetPasswordRequest;
import com.quiz.learning.Demo.domain.auth.forgotPassword.dto.ForgotPasswordResponse.EmailCheckingResponse;
import com.quiz.learning.Demo.domain.auth.forgotPassword.dto.ForgotPasswordResponse.ResetPasswordResponse;
import com.quiz.learning.Demo.service.auth.ForgotPasswordService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class ForgotPassword {
    private final ForgotPasswordService forgotPasswordService;

    public ForgotPassword(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService = forgotPasswordService;
    }

    @PostMapping("/request-otp")
    public ResponseEntity<EmailCheckingResponse> checkEmail(@RequestBody EmailCheckingRequest emailCheckingRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.forgotPasswordService.handleCheckEmail(emailCheckingRequest));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponse> postMethodName(
            @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.forgotPasswordService.handleResetPassword(resetPasswordRequest));
    }

}
