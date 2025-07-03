package com.quiz.learning.Demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quiz.learning.Demo.domain.auth.forgotPassword.PasswordResetToken;

@ResponseBody
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    public Optional<PasswordResetToken> findByEmail(String email);

    public Optional<PasswordResetToken> findByResetToken(String token);

    public void deleteByEmail(String email);
}
