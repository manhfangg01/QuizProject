package com.quiz.learning.Demo.service.auth;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.quiz.learning.Demo.domain.User;
import com.quiz.learning.Demo.domain.auth.LoginRequest;
import com.quiz.learning.Demo.domain.auth.LoginResponse;
import com.quiz.learning.Demo.domain.auth.SignupRequest;
import com.quiz.learning.Demo.domain.auth.SignupResponse;
import com.quiz.learning.Demo.repository.RoleRepository;
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

    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;

    private final SecurityUtil securityUtil;
    private final UserRepository userRepository;
    private final AdminUserService adminUserService;
    private final AdminRoleService adminRoleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtDecoder jwtDecoder;
    private final RoleRepository roleRepository;

    public AuthService(
            SecurityUtil securityUtil, UserRepository userRepository, AdminUserService adminUserService,
            AdminRoleService adminRoleService, PasswordEncoder passwordEncoder, JwtDecoder jwtDecoder,
            RoleRepository roleRepository) {
        this.securityUtil = securityUtil;
        this.userRepository = userRepository;
        this.adminUserService = adminUserService;
        this.adminRoleService = adminRoleService;
        this.passwordEncoder = passwordEncoder;
        this.jwtDecoder = jwtDecoder;
        this.roleRepository = roleRepository;

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
        loginResponse.setFullName(realUser.getFullName());
        loginResponse.setRole(realUser.getRole() != null ? realUser.getRole().getName() : "");
        loginResponse.setUserId(realUser.getId());
        loginResponse.setEmail(realUser.getEmail());
        loginResponse.setUserAvatarUrls(realUser.getUserAvatarUrls() != null ? realUser.getUserAvatarUrls() : "");
        loginResponse.setAccessToken(this.handleGenerateAccessToken(loginRequest));
        loginResponse.setAbout(realUser.getAbout());
        return loginResponse;
    }

    public SignupResponse handleSignUp(SignupRequest signupRequest) {
        if (!signupRequest.getConfirmPassword().equals(signupRequest.getPassword())) {
            throw new WrongCheckPassword("CheckedPassword is not correct");
        }

        Optional<User> checkUser = this.userRepository.findByFullName(signupRequest.getFullName());
        if (checkUser.isPresent()) {
            throw new DuplicatedObjectException("This user is already existed");
        }

        User newUser = new User();
        newUser.setEmail(signupRequest.getEmail());
        newUser.setFullName(signupRequest.getFullName());
        newUser.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        newUser.setCreatedAt(Instant.now());
        newUser.setCreatedBy(signupRequest.getEmail());
        newUser.setRole(this.adminRoleService.handleFetchRoleByName("USER"));

        User savedUser = this.userRepository.save(newUser);

        SignupResponse signupResponse = new SignupResponse();
        signupResponse.setUserId(savedUser.getId());
        signupResponse.setEmail(savedUser.getEmail());
        signupResponse.setFullName(savedUser.getFullName());
        signupResponse.setRole(savedUser.getRole().getName());
        signupResponse.setCreatedAt(savedUser.getCreatedAt());

        return signupResponse;
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

    public void updateUserToken(String token, String email) {
        Optional<User> checkUser = this.userRepository.findByEmail(email);

        User currentUser = checkUser.orElseThrow(() -> new ObjectNotFound("User log out not found"));
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public LoginResponse handleSocialLogin(String email, String name, String pictureUrl, String uid) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        User user = userOptional.orElseGet(() -> {
            // Tạo user mới nếu chưa tồn tại
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFullName(name);
            newUser.setUserAvatarUrls(pictureUrl);
            newUser.setRole(roleRepository.findByName("USER").get());
            newUser.setFirebaseUid(uid);

            return userRepository.save(newUser);
        });

        // Tạo access token
        String accessToken = securityUtil.createAccessToken(user.getEmail());

        // Tạo và trả về response theo cách thông thường
        LoginResponse response = new LoginResponse();
        response.setAccessToken(accessToken);
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setRole(user.getRole().getName());
        response.setUserId(user.getId());
        response.setUserAvatarUrls(user.getUserAvatarUrls());

        return response;
    }

    public void updateUserProfile(String uid, String name, String pictureUrl) {
        userRepository.findByFirebaseUid(uid).ifPresent(user -> {
            if (StringUtils.hasText(name))
                user.setFullName(name);
            if (StringUtils.hasText(pictureUrl))
                user.setUserAvatarUrls(pictureUrl);
            userRepository.save(user);
        });
    }

    public FirebaseToken verifyFirebaseToken(String idToken) {
        try {
            return FirebaseAuth.getInstance().verifyIdToken(idToken);
        } catch (Exception e) {
            System.err.println("Firebase token verification failed: " + e.getMessage());
            return null;
        }
    }

}
