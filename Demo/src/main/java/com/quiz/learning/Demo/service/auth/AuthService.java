package com.quiz.learning.Demo.service.auth;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import com.quiz.learning.Demo.domain.User;
import com.quiz.learning.Demo.domain.auth.LoginRequest;
import com.quiz.learning.Demo.domain.auth.LoginResponse;
import com.quiz.learning.Demo.domain.auth.RegisterRequest;
import com.quiz.learning.Demo.domain.auth.RegisterResponse;
import com.quiz.learning.Demo.domain.restResponse.ApiMessage;
import com.quiz.learning.Demo.repository.UserRepository;
import com.quiz.learning.Demo.service.admin.AdminRoleService;
import com.quiz.learning.Demo.service.admin.AdminUserService;
import com.quiz.learning.Demo.util.error.DuplicatedObjectException;
import com.quiz.learning.Demo.util.error.InvalidToken;
import com.quiz.learning.Demo.util.error.ObjectNotFound;
import com.quiz.learning.Demo.util.error.ResourceNotExisted;
import com.quiz.learning.Demo.util.error.WrongCheckPassword;
import com.quiz.learning.Demo.util.security.SecurityUtil;

@Service
public class AuthService {
    private final SecurityUtil securityUtil;
    private final UserRepository userRepository;
    private final AdminUserService adminUserService;
    private final AdminRoleService adminRoleService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(SecurityUtil securityUtil, UserRepository userRepository, AdminUserService adminUserService,
            AdminRoleService adminRoleService, PasswordEncoder passwordEncoder) {
        this.securityUtil = securityUtil;
        this.userRepository = userRepository;
        this.adminUserService = adminUserService;
        this.adminRoleService = adminRoleService;
        this.passwordEncoder = passwordEncoder;
    }

    public String handleCreateRefreshToken(String username) {
        String refreshToken = this.securityUtil.createRefreshToken(username);

        // update user
        this.adminUserService.updateUserRefreshToken(username, refreshToken);
        return refreshToken;
    }

    public String handleGenerateAccessToken(LoginRequest loginRequest) {

        String accessToken = this.securityUtil.createAccessToken(loginRequest.getUsername());
        return accessToken;
    }

    public LoginResponse handleGenerateLoginResponse(LoginRequest loginRequest) {
        LoginResponse loginResponse = new LoginResponse();
        Optional<User> checkUser = this.userRepository.findByEmail(loginRequest.getUsername());
        if (checkUser.isEmpty()) {
            throw new ObjectNotFound("User not found");
        }
        User realUser = checkUser.get();
        LoginResponse.UserLogin userLogin = new LoginResponse.UserLogin();
        userLogin.setEmail(realUser.getEmail());
        userLogin.setFullName(realUser.getFullName());
        userLogin.setRole(this.adminRoleService.handleFetchRoleById(realUser.getRole().getId()).getName());
        userLogin.setUserId(realUser.getId());
        loginResponse.setUser(userLogin);
        loginResponse.setAccessToken(this.handleGenerateAccessToken(loginRequest));
        return loginResponse;
    }

    public RegisterResponse handleRegister(RegisterRequest registerRequest) {
        if (!registerRequest.getCheckedPassword().equals(registerRequest.getPassword())) {
            throw new WrongCheckPassword("CheckedPassword is not correct");
        }

        Optional<User> checkUser = this.userRepository.findByFullName(registerRequest.getFullName());
        if (checkUser.isPresent()) {
            throw new DuplicatedObjectException("This user is already existed");
        }

        User newUser = new User();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setFullName(registerRequest.getFullName());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setCreatedAt(Instant.now());
        newUser.setCreatedBy(registerRequest.getEmail());
        newUser.setRole(this.adminRoleService.handleFetchRoleByName("USER"));

        User savedUser = this.userRepository.save(newUser);

        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setUserId(savedUser.getId());
        registerResponse.setEmail(savedUser.getEmail());
        registerResponse.setFullName(savedUser.getFullName());
        registerResponse.setRole(savedUser.getRole().getName());
        registerResponse.setCreatedAt(savedUser.getCreatedAt());

        return registerResponse;
    }

    public LoginResponse.UserLogin handleGetAccountInfor() {
        String email = SecurityUtil.getCurrentUserLogin().isEmpty() ? "" : SecurityUtil.getCurrentUserLogin().get();

        Optional<User> checkUser = this.userRepository.findByEmail(email);
        if (checkUser.isEmpty()) {
            throw new ObjectNotFound("User not found");
        }
        LoginResponse.UserLogin currentLoginUser = new LoginResponse.UserLogin();
        User realUser = checkUser.get();
        currentLoginUser.setUserId(realUser.getId());
        currentLoginUser.setEmail(realUser.getEmail());
        currentLoginUser.setFullName(realUser.getFullName());
        currentLoginUser.setRole(realUser.getRole() == null ? "" : realUser.getRole().getName());

        return currentLoginUser;
    }

    public LoginResponse handleRefreshTokens(String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new ResourceNotExisted("Resource refresh_token is unavailable");
        }
        Jwt jwt = securityUtil.checkValidRefreshToken(refreshToken);
        String email = jwt.getSubject();
        Optional<User> checkUser = this.userRepository.findByEmail(email);
        if (checkUser.isEmpty()) {
            throw new ObjectNotFound("User not found");
        }
        User realUser = checkUser.get();
        if (!refreshToken.equals(realUser.getRefreshToken())) {
            throw new InvalidToken("refresh is invalid");
        }
        String accessToken = this.securityUtil.createAccessToken(realUser.getEmail());
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(accessToken);
        LoginResponse.UserLogin userLogin = new LoginResponse.UserLogin();
        userLogin.setEmail(realUser.getEmail());
        userLogin.setFullName(realUser.getFullName());
        userLogin.setRole(realUser.getRole().getName());
        userLogin.setUserId(realUser.getId());
        loginResponse.setUser(userLogin);
        return loginResponse;
    }

    public void handleLogout() {
        // Lấy ra email người dùng
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        if (email.equals("")) {
            throw new InvalidToken("Access Token không hợp lệ");
        }

        // update refreshToke in DB
        this.adminUserService.updateUserRefreshToken(email, null);
    }

}
