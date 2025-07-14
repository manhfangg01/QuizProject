package com.quiz.learning.Demo.service.client;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.quiz.learning.Demo.domain.User;
import com.quiz.learning.Demo.domain.request.client.user.ClientRequestUserUpdate;
import com.quiz.learning.Demo.domain.response.client.profile.ProfileDTO;
import com.quiz.learning.Demo.domain.response.client.result.ClientResultDTO;
import com.quiz.learning.Demo.domain.response.client.user.ClientResponseUserUpdate;
import com.quiz.learning.Demo.repository.ResultRepository;
import com.quiz.learning.Demo.repository.UserRepository;
import com.quiz.learning.Demo.service.azure.AzureBlobService;
import com.quiz.learning.Demo.util.error.InvalidUploadedFile;
import com.quiz.learning.Demo.util.error.ObjectNotFound;
import com.quiz.learning.Demo.util.security.SecurityUtil;

@Service
public class ClientUserService {
    private final UserRepository userRepository;
    private final AzureBlobService azureBlobService;
    private final ResultRepository resultRepository;

    public ClientUserService(UserRepository userRepository, AzureBlobService azureBlobService,
            ResultRepository resultRepository) {
        this.userRepository = userRepository;
        this.azureBlobService = azureBlobService;
        this.resultRepository = resultRepository;
    }

    public Pageable handlePagination(int page, int size, String sortBy, String order) {
        // Validate input parameters
        page = Math.max(page, 1); // Đảm bảo page >= 1
        size = Math.max(size, 1); // Đảm bảo size >= 1

        Sort sort = order.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(page - 1, size, sort);
    }

    public List<ClientResultDTO> handleFetchResults(int page, int size, String sortBy, String order) {
        return this.resultRepository.findAll(this.handlePagination(page, size, sortBy, order)).stream().map(res -> {
            ClientResultDTO dto = new ClientResultDTO();
            dto.setDuration(res.getDuration());
            dto.setScore(res.getScore());
            dto.setSubmittedAt(res.getSubmittedAt());
            dto.setTitle(res.getQuiz() == null ? "" : res.getQuiz().getTitle());
            return dto;
        }).collect(Collectors.toList());
    }

    public ProfileDTO handleFetchProfile(Long id) {
        Optional<User> checkUser = this.userRepository.findById(id);
        if (checkUser.isEmpty()) {
            throw new ObjectNotFound("Người dùng không tồn tại");
        }
        User realUser = checkUser.get();
        ProfileDTO dto = new ProfileDTO();
        dto.setAvatar(realUser.getUserAvatarUrls());
        dto.setUsername(realUser.getFullName());
        dto.setAbout(realUser.getAbout());
        dto.setEmail(realUser.getEmail());
        return dto;
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

    public ClientResponseUserUpdate handleUpdate(ClientRequestUserUpdate request, MultipartFile userAvatar) {
        Optional<User> checkUser = this.userRepository.findById(request.getId());
        if (checkUser.isEmpty()) {
            throw new ObjectNotFound("Người dùng không tồn tại");
        }
        User realUser = checkUser.get();
        realUser.setAbout(request.getAbout());
        realUser.setUpdatedAt(Instant.now());
        realUser.setUpdatedBy(SecurityUtil.getCurrentUserLogin().get());
        realUser.setFullName(request.getFullName());
        this.handleAssginingUserAvatar(realUser, userAvatar);
        this.userRepository.save(realUser);
        ClientResponseUserUpdate response = new ClientResponseUserUpdate();
        response.setAbout(request.getAbout());
        response.setFullName(request.getFullName());
        response.setId(request.getId());
        response.setAvatar(realUser.getUserAvatarUrls());
        return response;
    }

}
