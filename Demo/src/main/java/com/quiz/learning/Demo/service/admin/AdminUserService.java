package com.quiz.learning.Demo.service.admin;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Result;
import com.quiz.learning.Demo.domain.Role;
import com.quiz.learning.Demo.domain.User;
import com.quiz.learning.Demo.domain.request.admin.user.CreateUserRequest;
import com.quiz.learning.Demo.domain.request.admin.user.UpdateUserRequest;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchUserDTO;
import com.quiz.learning.Demo.repository.ResultRepository;
import com.quiz.learning.Demo.repository.UserRepository;
import com.quiz.learning.Demo.util.error.DuplicatedObjectException;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

@Service
public class AdminUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResultRepository resultRepository;

    public AdminUserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            ResultRepository resultRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.resultRepository = resultRepository;
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

        // Lấy danh sách tên role
        if (user.getRoles() != null) {
            Set<String> roleNames = user.getRoles()
                    .stream()
                    .map(Role::getName) // giả sử Role có getName()
                    .collect(Collectors.toSet());
            dto.setRoles(roleNames);
        }
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

    public FetchAdminDTO.FetchUserDTO handleCreateUser(CreateUserRequest newUser) {
        if (this.userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new DuplicatedObjectException("Duplicated Email");
        }
        if (this.userRepository.findByFullName(newUser.getFullName()).isPresent()) {
            throw new DuplicatedObjectException("Duplicated UserName");
        }
        User user = new User();
        user.setEmail(newUser.getEmail());
        user.setFullName(newUser.getFullName());
        user.setCreatedAt(Instant.now());
        user.setCreatedBy("hardcode");
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setRoles(null);
        return this.convertToDTO(this.userRepository.save(user));
    }

    public FetchAdminDTO.FetchUserDTO handleUpdateUser(UpdateUserRequest updatedUser) {
        Optional<User> checkUser = this.userRepository.findById(updatedUser.getId());
        if (checkUser.isEmpty()) {
            throw new ObjectNotFound("There is no user has id: " + updatedUser.getId());
        }
        User realUser = checkUser.get();
        if (!realUser.getFullName().equalsIgnoreCase(updatedUser.getFullName())) {
            if (this.userRepository.findByFullName(updatedUser.getFullName()).isPresent()) {
                throw new DuplicatedObjectException("Duplicated UserName");
            }
        }

        realUser.setFullName(updatedUser.getFullName());
        realUser.setRoles(null);
        realUser.setUpdatedAt(Instant.now());
        realUser.setUpdatedBy("HardCode");
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
        this.userRepository.delete(realUser);
    }

}
