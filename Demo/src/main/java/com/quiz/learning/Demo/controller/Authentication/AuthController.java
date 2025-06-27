package com.quiz.learning.Demo.controller.authentication;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.oauth2.jwt.Jwt;

import com.quiz.learning.Demo.domain.auth.LoginRequest;
import com.quiz.learning.Demo.domain.auth.LoginResponse;
import com.quiz.learning.Demo.domain.auth.RegisterRequest;
import com.quiz.learning.Demo.domain.auth.RegisterResponse;
import com.quiz.learning.Demo.domain.restResponse.ApiMessage;
import com.quiz.learning.Demo.service.auth.AuthService;
import com.quiz.learning.Demo.util.error.ResourceNotExisted;
import com.quiz.learning.Demo.util.security.SecurityUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final SecurityUtil securityUtil;

    public AuthController(AuthenticationManager authenticationManager, AuthService authService,
            SecurityUtil securityUtil) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.securityUtil = securityUtil;

    }

    @Value("${RefreshToken-Validity-In-Seconds}")
    private long refreshTokenExpiration;

    @PostMapping("/register")
    @ApiMessage("register account")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(this.authService.handleRegister(registerRequest));
    }

    @PostMapping("/login")
    @ApiMessage("Login with email and password")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
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
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(loginResponse);
    }

    @PostMapping("/logout")
    @ApiMessage("Log out user")
    public ResponseEntity<Void> logoutUser() { // Sau API này thì chỉ có refreshToken bị xóa nên AccessToken vẫn dùng
                                               // được, điều này là đúng trong mô hình stateless

        this.authService.handleLogout();
        // remove refresh token cookie
        ResponseCookie deleteSpringCookie = ResponseCookie
                .from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
                .body(null);
    }

}
