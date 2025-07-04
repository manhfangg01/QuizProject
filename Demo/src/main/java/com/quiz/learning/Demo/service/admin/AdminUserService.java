package com.quiz.learning.Demo.service.admin;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.quiz.learning.Demo.domain.User;
import com.quiz.learning.Demo.domain.metadata.Metadata;
import com.quiz.learning.Demo.domain.request.admin.user.CreateUserRequest;
import com.quiz.learning.Demo.domain.request.admin.user.UpdateUserRequest;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchUserDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchUserPaginationDTO;
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

    private final AdminRoleService adminRoleService;
    private final AzureBlobService azureBlobService;

    public AdminUserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AdminRoleService adminRoleService,
            AzureBlobService azureBlobService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminRoleService = adminRoleService;
        this.azureBlobService = azureBlobService;

    }

    public User handleGetUser(Long id) {
        Optional<User> checkUser = this.userRepository.findById(id);
        if (checkUser.isEmpty()) {
            throw new ObjectNotFound("User with id: " + id + " is not existed");
        }
        return checkUser.get();
    }

    public void handleSaveUser(User user) {
        this.userRepository.save(user);
    }

    public User handleGetUserByEmail(String email) {
        Optional<User> checkUser = this.userRepository.findByEmail(email);
        if (checkUser.isEmpty()) {
            throw new ObjectNotFound("User with id: " + email + " is not existed");
        }
        return checkUser.get();
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

    public boolean isPrime(long n) {
        if (n <= 1)
            return false;
        if (n == 2)
            return true;
        if (n % 2 == 0)
            return false;

        int sqrt = (int) Math.sqrt(n);
        for (int i = 3; i <= sqrt; i += 2) {
            if (n % i == 0)
                return false;
        }
        return true;
    }

    public int maxDivisorOfNUnder10(long n) {
        for (int i = 10; i >= 1; i--) {
            if (n % i == 0) {
                return i;
            }
        }
        return -1; // Trường hợp bất thường, nếu n < 1
    }

    public FetchUserPaginationDTO handleFetchAllUsers(int page, int size, String sortBy, String order) {

        Sort sort = order.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        // Logic phân trang theo tổng số
        long totalUsers = this.userRepository.count();
        if (totalUsers != 0 && !isPrime(totalUsers) && totalUsers > 10) {
            size = maxDivisorOfNUnder10(totalUsers);
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> pageUsers = this.userRepository.findAll(pageable);
        List<User> users = pageUsers.getContent();
        // Gán DTO
        FetchUserPaginationDTO dto = new FetchUserPaginationDTO();
        Metadata metadata = new Metadata();
        metadata.setCurrentPage(page);
        metadata.setPageSize(size);
        metadata.setTotalObjects(Long.valueOf(pageUsers.getTotalElements()));
        metadata.setTotalPages(pageUsers.getTotalPages() - 1);
        metadata.setHasNext(page < pageUsers.getTotalPages() - 1);
        metadata.setHasPrevious(page > 1);

        dto.setMetadata(metadata);

        dto.setUsers(users.stream().map(this::convertToDTO).collect(Collectors.toList()));

        return dto;
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
        if ((realUser.getFullName() != null)) {
            if (!realUser.getFullName().equalsIgnoreCase(updatedUser.getFullName())) {
                if (this.userRepository.findByFullName(updatedUser.getFullName()).isPresent()) {
                    throw new DuplicatedObjectException("Duplicated UserName");
                }
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
