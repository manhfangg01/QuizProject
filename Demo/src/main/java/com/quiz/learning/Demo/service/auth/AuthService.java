package com.quiz.learning.Demo.service.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.User;
import com.quiz.learning.Demo.domain.auth.LoginRequest;
import com.quiz.learning.Demo.domain.auth.LoginResponse;
import com.quiz.learning.Demo.repository.UserRepository;
import com.quiz.learning.Demo.service.admin.AdminRoleService;
import com.quiz.learning.Demo.service.admin.AdminUserService;
import com.quiz.learning.Demo.util.error.ObjectNotFound;
import com.quiz.learning.Demo.util.security.SecurityUtil;

@Service
public class AuthService {
    private final SecurityUtil securityUtil;
    private final UserRepository userRepository;
    private final AdminUserService adminUserService;
    private final AdminRoleService adminRoleService;

    public AuthService(SecurityUtil securityUtil, UserRepository userRepository, AdminUserService adminUserService,
            AdminRoleService adminRoleService) {
        this.securityUtil = securityUtil;
        this.userRepository = userRepository;
        this.adminUserService = adminUserService;
        this.adminRoleService = adminRoleService;
    }

    public String handleCreateRefreshToken(LoginRequest loginRequest) {
        String refreshToken = this.securityUtil.createRefreshToken(loginRequest);

        // update user
        this.adminUserService.updateUserRefreshToken(loginRequest, refreshToken);
        return refreshToken;
    }

    public String handleGenerateAccessToken(LoginRequest loginRequest) {

        String accessToken = this.securityUtil.createAccessToken(loginRequest);
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
        userLogin.setRole(this.adminRoleService.handleFetchRoleById(realUser.getRole().getId()));
        userLogin.setUserId(realUser.getId());
        loginResponse.setUser(userLogin);
        loginResponse.setAccessToken(this.handleGenerateAccessToken(loginRequest));
        return loginResponse;
    }

}
