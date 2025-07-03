package com.quiz.learning.Demo.service.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quiz.learning.Demo.domain.User;
import com.quiz.learning.Demo.domain.auth.forgotPassword.PasswordResetToken;
import com.quiz.learning.Demo.domain.auth.forgotPassword.dto.ForgotPasswordRequest.EmailCheckingRequest;
import com.quiz.learning.Demo.domain.auth.forgotPassword.dto.ForgotPasswordRequest.ResetPasswordRequest;
import com.quiz.learning.Demo.domain.auth.forgotPassword.dto.ForgotPasswordResponse.EmailCheckingResponse;
import com.quiz.learning.Demo.domain.auth.forgotPassword.dto.ForgotPasswordResponse.ResetPasswordResponse;
import com.quiz.learning.Demo.domain.restResponse.RestResponse;
import com.quiz.learning.Demo.repository.PasswordResetTokenRepository;
import com.quiz.learning.Demo.service.admin.AdminUserService;
import com.quiz.learning.Demo.util.error.EmailSendingException;
import com.quiz.learning.Demo.util.error.InvalidResetTokenException;

import jakarta.mail.internet.MimeMessage;

@Service
public class ForgotPasswordService {

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;
    private final AdminUserService adminUserService;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final JavaMailSender javaMailSender;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public ForgotPasswordService(AdminUserService adminUserService, JwtEncoder jwtEncoder,
            JavaMailSender javaMailSender, PasswordResetTokenRepository passwordResetTokenRepository,
            JwtDecoder jwtDecoder, PasswordEncoder passwordEncoder) {
        this.adminUserService = adminUserService;
        this.jwtEncoder = jwtEncoder;
        this.javaMailSender = javaMailSender;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.jwtDecoder = jwtDecoder;
        this.passwordEncoder = passwordEncoder;
    }

    public String createAccessToken(String username) {
        Instant now = Instant.now();
        Instant expiry = now.plus(15, ChronoUnit.MINUTES); // reset token sống 15 phút

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(expiry)
                .subject(username)
                .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        String resetToken = this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setCreateAt(now);
        passwordResetToken.setExpiryAt(expiry);
        passwordResetToken.setResetToken(resetToken);
        passwordResetToken.setEmail(username);
        this.passwordResetTokenRepository.deleteByEmail(username); // Xóa cái cũ nếu có
        this.passwordResetTokenRepository.save(passwordResetToken);

        return resetToken;
    }

    public void handleSendEmailWithResetLink(String username, String resetToken) {
        String resetLink = "http://localhost:3000/reset-password?token=" + resetToken;

        String content = "<p>Vui lòng click vào đường dẫn để đổi mật khẩu: <strong>" + resetLink + "</strong></p>"
                + "<p>Đường liên kết có hiệu lực trong 15 phút.</p>";

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(username);
            helper.setSubject("Phản hồi yêu cầu đổi mật khẩu");
            helper.setText(content, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            System.err.println("Lỗi gửi email reset mật khẩu: " + e.getMessage());
            throw new EmailSendingException(e.getMessage());
        }
    }

    public EmailCheckingResponse handleCheckEmail(EmailCheckingRequest emailCheckingRequest) {
        String email = emailCheckingRequest.getUsername();

        // Luôn khởi tạo response mặc định
        // Luôn khởi tạo một response mặc định hacker không dò được email đã tồn tại =>
        // tăng bảo mật
        EmailCheckingResponse response = new EmailCheckingResponse();
        response.setMessage("Nếu email tồn tại, một đường link khôi phục mật khẩu đã được gửi tới hộp thư của bạn");

        // Nếu user tồn tại thì gửi reset link
        Optional<User> userOptional = this.adminUserService.handleFetchUserByUsername(email);
        if (userOptional.isPresent()) {
            String resetToken = this.createAccessToken(email);
            this.handleSendEmailWithResetLink(email, resetToken);
        }

        return response;
    }

    public ResetPasswordResponse handleResetPassword(ResetPasswordRequest resetPasswordRequest) {
        String token = resetPasswordRequest.getResetToken();

        // 1. Giải mã token
        Jwt jwt;
        try {
            jwt = this.jwtDecoder.decode(token);
        } catch (JwtException e) {
            throw new InvalidResetTokenException("Reset token không hợp lệ hoặc đã hết hạn");
        }

        String email = jwt.getSubject();
        Instant expiry = jwt.getExpiresAt();
        if (expiry.isBefore(Instant.now())) {
            throw new InvalidResetTokenException("Reset token đã hết hạn");
        }

        // 2. Kiểm tra token còn tồn tại trong DB (nếu bạn lưu vào bảng
        // PasswordResetToken)

        Optional<PasswordResetToken> tokenOptional = passwordResetTokenRepository.findByResetToken(token);
        if (tokenOptional.isEmpty()) {
            throw new InvalidResetTokenException("Token đã được sử dụng hoặc không hợp lệ");
        }

        // 3. Cập nhật mật khẩu
        User user = adminUserService.handleGetUserByEmail(email);
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        adminUserService.handleSaveUser(user); // bạn cần viết thêm saveUser nếu chưa có

        // 4. Xoá token khỏi DB (nếu chỉ dùng một lần)
        passwordResetTokenRepository.delete(tokenOptional.get());

        return new ResetPasswordResponse("Đổi mật khẩu thành công!");

    }
}
