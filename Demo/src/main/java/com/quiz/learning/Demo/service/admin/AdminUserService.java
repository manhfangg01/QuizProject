package com.quiz.learning.Demo.service.admin;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.quiz.learning.Demo.domain.Result;
import com.quiz.learning.Demo.domain.User;
import com.quiz.learning.Demo.domain.request.admin.user.CreateUserRequest;
import com.quiz.learning.Demo.domain.request.admin.user.UpdateUserRequest;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchUserDTO;
import com.quiz.learning.Demo.repository.ResultRepository;
import com.quiz.learning.Demo.repository.UserRepository;
import com.quiz.learning.Demo.service.azure.AzureBlobService;
import com.quiz.learning.Demo.util.error.DuplicatedObjectException;
import com.quiz.learning.Demo.util.error.InvalidUploadedFile;
import com.quiz.learning.Demo.util.error.ObjectNotFound;
import com.quiz.learning.Demo.util.security.SecurityUtil;

@Service
public class AdminUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResultRepository resultRepository;
    private final AdminRoleService adminRoleService;
    private final AzureBlobService azureBlobService;

    public AdminUserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            ResultRepository resultRepository, AdminRoleService adminRoleService, AzureBlobService azureBlobService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.resultRepository = resultRepository;
        this.adminRoleService = adminRoleService;
        this.azureBlobService = azureBlobService;

    }

    public FetchAdminDTO.FetchUserDTO convertToDTO(User user) {
        FetchUserDTO dto = new FetchUserDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setCreatedBy(user.getCreatedBy());
        dto.setUpdatedBy(user.getUpdatedBy());
        dto.setRole(this.adminRoleService.handleFetchRoleById(user.getRole().getId()).getName());
        dto.setUserAvatarUrls(user.getUserAvatarUrls());
        return dto;
    }

    public List<FetchAdminDTO.FetchUserDTO> handleFetchAllUsers() {
        return this.userRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public FetchAdminDTO.FetchUserDTO handleFetchOneUser(Long id) {
        Optional<User> checkUser = this.userRepository.findById(id);
        if (checkUser.isEmpty()) {
            throw new ObjectNotFound("There is no user has id: " + id);
        }
        return this.convertToDTO(checkUser.get());
    }

    public void handleAssginingUserAvatar(User user, MultipartFile userAvatar) {
        try {
            if (userAvatar != null && !userAvatar.isEmpty()) {
                String fileName = userAvatar.getOriginalFilename();
                List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx", "webp");
                boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));
                if (!isValid) {
                    throw new InvalidUploadedFile(
                            "Invalid file extension. only allows: " + allowedExtensions.toString());
                }
                String imageUrl = azureBlobService.uploadFile(userAvatar);
                user.setUserAvatarUrls(imageUrl);
            } else {
                String defaultAvatar = "https://quizblobs.blob.core.windows.net/applicant-photos/default-user.webp";
                user.setUserAvatarUrls(defaultAvatar);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload avatar", e);
        }

    }

    public FetchAdminDTO.FetchUserDTO handleCreateUser(CreateUserRequest newUser, MultipartFile userAvatar) {
        if (this.userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new DuplicatedObjectException("Email: " + newUser.getEmail() + " is duplicated");
        }
        if (this.userRepository.findByFullName(newUser.getFullName()).isPresent()) {
            throw new DuplicatedObjectException("Duplicated UserName");
        }
        User user = new User();
        user.setEmail(newUser.getEmail());
        user.setFullName(newUser.getFullName());
        user.setCreatedAt(Instant.now());
        user.setCreatedBy(SecurityUtil.getCurrentUserLogin().get());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setRole(this.adminRoleService.handleFetchRoleByName(newUser.getRole()));
        this.handleAssginingUserAvatar(user, userAvatar);
        return this.convertToDTO(this.userRepository.save(user));
    }

    public FetchAdminDTO.FetchUserDTO handleUpdateUser(UpdateUserRequest updatedUser, MultipartFile userAvatar) {
        Optional<User> checkUser = this.userRepository.findById(updatedUser.getUserId());
        if (checkUser.isEmpty()) {
            throw new ObjectNotFound("There is no user has id: " + updatedUser.getUserId());
        }
        User realUser = checkUser.get();
        if (!realUser.getFullName().equalsIgnoreCase(updatedUser.getFullName())) {
            if (this.userRepository.findByFullName(updatedUser.getFullName()).isPresent()) {
                throw new DuplicatedObjectException("Duplicated UserName");
            }
        }

        realUser.setFullName(updatedUser.getFullName());
        realUser.setRole(this.adminRoleService.handleFetchRoleByName(updatedUser.getRole()));
        realUser.setUpdatedAt(Instant.now());
        realUser.setUpdatedBy(SecurityUtil.getCurrentUserLogin().get());
        if (userAvatar != null) {
            try {

                if (userAvatar != null && !userAvatar.isEmpty()) {
                    String imageUrl = azureBlobService.uploadFile(userAvatar);
                    realUser.setUserAvatarUrls(imageUrl);
                }

            } catch (IOException e) {
                throw new RuntimeException("Failed to upload avatar", e);
            }
        }
        return this.convertToDTO(this.userRepository.save(realUser));
    }

    public void handleDeleteUser(Long id) {
        Optional<User> checkUser = this.userRepository.findById(id);
        if (checkUser.isEmpty()) {
            throw new ObjectNotFound("There is no user has id: " + id);
        }
        User realUser = checkUser.get();
        if (realUser.getResults() != null) {
            for (Result result : realUser.getResults()) {
                this.resultRepository.delete(result);
            }
        }

        if (realUser.getUserAvatarUrls() != null && !realUser.getUserAvatarUrls().isEmpty()) {
            String blobName = azureBlobService.getBlobNameFromUrl(realUser.getUserAvatarUrls());
            if (blobName != null) {
                azureBlobService.deleteFile(blobName);
            }
        }
        this.userRepository.delete(realUser);
    }

    public Optional<User> handleFetchUserByUsername(String email) {
        return this.userRepository.findByEmail(email);
    }

    public void updateUserRefreshToken(String username, String token) {
        Optional<User> checkUser = this.userRepository.findByEmail(username);
        if (checkUser.isEmpty()) {
            throw new ObjectNotFound("User with provided email not existed");
        }
        User realUser = checkUser.get();
        realUser.setRefreshToken(token);
        this.userRepository.save(realUser);
    }

}
