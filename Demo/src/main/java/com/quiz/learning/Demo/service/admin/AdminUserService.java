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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.quiz.learning.Demo.domain.User;
import com.quiz.learning.Demo.domain.filterCriteria.UserFilter;
import com.quiz.learning.Demo.domain.metadata.Metadata;
import com.quiz.learning.Demo.domain.request.admin.user.CreateUserRequest;
import com.quiz.learning.Demo.domain.request.admin.user.UpdateUserRequest;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchUserDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchUserPaginationDTO;
import com.quiz.learning.Demo.repository.UserRepository;
import com.quiz.learning.Demo.service.azure.AzureBlobService;
import com.quiz.learning.Demo.service.specification.UserSpecs;
import com.quiz.learning.Demo.util.error.DuplicatedObjectException;
import com.quiz.learning.Demo.util.error.InvalidUploadedFile;
import com.quiz.learning.Demo.util.error.ObjectNotFound;
import com.quiz.learning.Demo.util.security.SecurityUtil;

@Service
public class AdminUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserSpecs userSpecs;
    private final AdminRoleService adminRoleService;
    private final AzureBlobService azureBlobService;

    public AdminUserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AdminRoleService adminRoleService,
            AzureBlobService azureBlobService, UserSpecs userSpecs) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminRoleService = adminRoleService;
        this.azureBlobService = azureBlobService;
        this.userSpecs = userSpecs;

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

    public Pageable handlePagination(int page, int size, String sortBy, String order) {
        // Validate page and size
        page = page < 1 ? 1 : page;
        size = size < 1 ? 10 : size;
        size = size > 100 ? 100 : size; // Giới hạn tối đa 100 items/page

        Sort sort = order.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(page - 1, size, sort);
    }

    public FetchUserPaginationDTO handleFetchAllUsers(int page, int size, String sortBy, String order,
            UserFilter filterCriteria) {

        Pageable pageable = this.handlePagination(page, size, sortBy, order);
        Specification<User> spec = (root, query, cb) -> cb.conjunction();
        Specification<User> spec1 = this.userSpecs.hasId(filterCriteria.getId());
        Specification<User> spec2 = this.userSpecs.hasEmailLike(filterCriteria.getEmail());
        Specification<User> spec3 = this.userSpecs.hasRole(filterCriteria.getRole());
        Specification<User> spec4 = this.userSpecs.nameLike(filterCriteria.getFullName());
        spec = spec.and(spec1).and(spec2).and(spec3).and(spec4);

        Page<User> pageUsers = this.userRepository.findAll(spec, pageable);
        List<User> users = pageUsers.getContent();

        // Gán DTO
        FetchUserPaginationDTO dto = new FetchUserPaginationDTO();
        Metadata metadata = new Metadata();
        metadata.setCurrentPage(page);
        metadata.setPageSize(size);
        metadata.setTotalObjects(pageUsers.getTotalElements());
        metadata.setTotalPages(pageUsers.getTotalPages());
        metadata.setHasNext(pageUsers.hasNext());
        metadata.setHasPrevious(pageUsers.hasPrevious());

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
