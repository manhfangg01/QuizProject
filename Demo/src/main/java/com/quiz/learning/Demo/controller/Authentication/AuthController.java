package com.quiz.learning.Demo.controller.authentication;

import org.springframework.web.bind.annotation.RestController;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.quiz.learning.Demo.domain.auth.LoginRequest;
import com.quiz.learning.Demo.domain.auth.LoginResponse;
import com.quiz.learning.Demo.domain.auth.SignupRequest;
import com.quiz.learning.Demo.domain.auth.SignupResponse;
import com.quiz.learning.Demo.domain.restResponse.ApiMessage;
import com.quiz.learning.Demo.service.admin.AdminUserService;
import com.quiz.learning.Demo.service.auth.AuthService;
import com.quiz.learning.Demo.service.client.ClientQuizService;
import com.quiz.learning.Demo.util.error.InvalidToken;
import com.quiz.learning.Demo.util.error.UnauthorizedException;
import com.quiz.learning.Demo.util.security.SecurityUtil;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

        private final AuthenticationManager authenticationManager;
        private final AuthService authService;
        private final SecurityUtil securityUtil;
        private final UserDetailsService userDetailsService;
        private final AdminUserService adminUserService;

        public AuthController(AuthenticationManager authenticationManager, AuthService authService,
                        SecurityUtil securityUtil, UserDetailsService userDetailsService,
                        AdminUserService adminUserService) {
                this.authenticationManager = authenticationManager;
                this.authService = authService;
                this.securityUtil = securityUtil;
                this.userDetailsService = userDetailsService;
                this.adminUserService = adminUserService;

        }

        @Value("${RefreshToken-Validity-In-Seconds}")
        private long refreshTokenExpiration;

        @PostMapping("/signup")
        @ApiMessage("Sign up an account")
        public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {
                return ResponseEntity.status(HttpStatus.OK).body(this.authService.handleSignUp(signupRequest));
        }

        @PostMapping("/login")
        @ApiMessage("Login with email and password")
        public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
                // Tạo token xác thực từ username/password
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(), loginRequest.getPassword());

                // Xác thực người dùng
                Authentication authentication = authenticationManager.authenticate(authenticationToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String refreshToken = this.authService.handleCreateRefreshToken(loginRequest.getUsername());
                // Tạo cookie chứa refresh token
                ResponseCookie responseCookie = ResponseCookie
                                .from("refresh_token", refreshToken)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(refreshTokenExpiration)
                                .build();

                return ResponseEntity.status(HttpStatus.OK)
                                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                                .body(this.authService.handleGenerateLoginResponse(loginRequest));
        }

        @GetMapping("/account")
        @ApiMessage("get current account information")
        public ResponseEntity<LoginResponse.UserLogin> getAccount() {
                return ResponseEntity.status(HttpStatus.OK).body(this.authService.handleGetAccountInfor());
        }

        @GetMapping("/refresh")
        public ResponseEntity<LoginResponse> getRefreshToken(@CookieValue(name = "refresh_token") String refreshToken) {
                // 1. Xác thực token cũ & sinh access token mới
                LoginResponse loginResponse = this.authService.handleRefreshTokens(refreshToken);

                // 2. Tạo refresh_token mới
                String email = loginResponse.getUser().getEmail();
                String newRefreshToken = this.authService.handleCreateRefreshToken(email);

                // 3. Gán vào Cookie
                ResponseCookie responseCookie = ResponseCookie
                                .from("refresh_token", newRefreshToken)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(refreshTokenExpiration)
                                .build();

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                                .body(loginResponse);
        }

        @PostMapping("/logout")
        @ApiMessage("Log out user")
        public ResponseEntity<Void> logoutUser() {
                String username = SecurityUtil.getCurrentUserLogin().orElse(null);

                this.authService.updateUserToken(null, username);

                // remove refresh token cookie
                ResponseCookie deleteSpringCookie = ResponseCookie
                                .from("refresh_token", null)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(0)
                                .sameSite("Strict") // <- thêm dòng này
                                .build();

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
                                .body(null);
        }

        // Test
        @GetMapping("/me")
        public ResponseEntity<?> getCurrentUser() {
                String token = SecurityUtil.getCurrentUserJWT().orElse(null);
                String username = SecurityUtil.getCurrentUserLogin().orElse(null);

                if (username == null || token == null) {
                        String reason = "";
                        reason += username;
                        reason += token;
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                        .body("User not authenticated due to " + reason);
                }

                return ResponseEntity.ok(Map.of(
                                "username", username,
                                "jwt", token));

        }

        @PostMapping("/social-login")
        @ApiMessage("Login with Firebase")
        public ResponseEntity<LoginResponse> loginWithFirebase(
                        @RequestBody Map<String, String> request,
                        HttpServletResponse servletResponse) {

                Logger logger = LoggerFactory.getLogger(AuthController.class);
                // 1. Lấy Firebase ID Token từ request
                String firebaseToken = request.get("token");
                if (!StringUtils.hasText(firebaseToken)) {
                        throw new InvalidToken("Firebase token is required");
                }

                try {
                        // 2. Xác thực token với Firebase Admin SDK
                        FirebaseToken decodedToken = FirebaseAuth.getInstance()
                                        .verifyIdToken(firebaseToken);

                        // 3. Lấy thông tin user từ token
                        String email = decodedToken.getEmail();
                        String name = decodedToken.getName();
                        String pictureUrl = decodedToken.getPicture();
                        String uid = decodedToken.getUid();

                        if (email == null) {
                                throw new InvalidToken("Firebase token must contain email");
                        }

                        // 4. Xử lý đăng nhập/đăng ký
                        LoginResponse loginResponse = authService.handleSocialLogin(
                                        email,
                                        name != null ? name : email.split("@")[0],
                                        pictureUrl, uid);

                        // 5. Tạo refresh token và lưu vào database
                        String refreshToken = authService.handleCreateRefreshToken(email);

                        // 6. Tạo HTTP Only cookie
                        ResponseCookie responseCookie = ResponseCookie
                                        .from("refresh_token", refreshToken)
                                        .httpOnly(true)
                                        .secure(true)
                                        .path("/")
                                        .maxAge(refreshTokenExpiration)
                                        .build();

                        // 7. Cập nhật thông tin user (nếu cần)
                        authService.updateUserProfile(uid, name, pictureUrl);

                        // Tạo authentication và set vào SecurityContext
                        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities());

                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        // 8. Trả về response
                        return ResponseEntity.ok()
                                        .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                                        .body(loginResponse);

                } catch (FirebaseAuthException e) {
                        logger.error("ERROR >>> Firebase Auth Error: {}", e.getErrorCode());
                        throw new InvalidToken(e.getMessage());
                } catch (Exception e) {
                        throw new UnauthorizedException("Authentication failed: " + e.getMessage());
                }
        }

}
