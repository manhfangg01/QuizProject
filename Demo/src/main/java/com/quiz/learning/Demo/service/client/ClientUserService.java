package com.quiz.learning.Demo.service.client;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.quiz.learning.Demo.domain.Answer;
import com.quiz.learning.Demo.domain.Quiz;
import com.quiz.learning.Demo.domain.Result;
import com.quiz.learning.Demo.domain.User;
import com.quiz.learning.Demo.domain.request.client.user.ClientRequestUserUpdate;
import com.quiz.learning.Demo.domain.response.client.profile.ProfileDTO;
import com.quiz.learning.Demo.domain.response.client.statistics.ClientStatistics;
import com.quiz.learning.Demo.domain.response.client.statistics.ClientStatistics.DoneResult;
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
        if (realUser.getRole().getName().equals("ADMIN")) {
            dto.setCreatedAt(realUser.getCreatedAt());
        }
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

    // Thống kê

    public ClientStatistics handleFetchUserStatistics() {
        // 1. Xác thực và lấy thông tin người dùng
        User user = getAuthenticatedUser();

        // 2. Lấy danh sách kết quả và kiểm tra
        List<Result> results = Optional.ofNullable(user.getResults())
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new ObjectNotFound("No results found for user"));

        // 3. Tính toán các thống kê
        StatisticsSummary summary = calculateSummaryStatistics(results);

        // 4. Tạo và trả về DTO
        return buildClientStatistics(results, summary);
    }

    // Helper methods
    private User getAuthenticatedUser() {
        String email = SecurityUtil.getCurrentUserLogin()
                .filter(e -> !e.isEmpty() && !"anonymousUser".equals(e))
                .orElseThrow(() -> new ObjectNotFound("Unauthenticated user"));

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFound("User not found"));
    }

    private StatisticsSummary calculateSummaryStatistics(List<Result> results) {
        long totalTimeLimit = 0;
        long totalDuration = 0;
        int totalCorrected = 0;
        int totalAnswers = 0;
        int totalScore = 0;
        double highestScore = 0;

        for (Result res : results) {
            Quiz quiz = res.getQuiz();
            if (quiz != null) {
                totalTimeLimit += quiz.getTimeLimit();
            }
            totalDuration += res.getDuration();

            double score = res.getScore();
            highestScore = Math.max(highestScore, score);
            totalScore += score;

            if (res.getAnswers() != null) {
                for (Answer ans : res.getAnswers()) {
                    if (Boolean.TRUE.equals(ans.getIsCorrect())) {
                        totalCorrected++;
                    }
                    totalAnswers++;
                }
            }
        }

        double accuracy = totalAnswers > 0 ? (double) totalCorrected / totalAnswers : 0;
        double averageScore = !results.isEmpty() ? (double) totalScore / results.size() : 0;
        double averageTime = totalTimeLimit > 0 ? (double) totalDuration / totalTimeLimit : 0;

        return new StatisticsSummary(
                results.size(),
                totalDuration,
                accuracy,
                averageTime,
                averageScore,
                highestScore);
    }

    private ClientStatistics buildClientStatistics(List<Result> results, StatisticsSummary summary) {
        ClientStatistics dto = new ClientStatistics();

        // Set basic statistics
        dto.setTotalDoneQuizzes(summary.totalQuizzes());
        dto.setTotalSpentTime(summary.totalDuration());
        dto.setAccuracy(summary.accuracy());
        dto.setAverageTime((long) summary.averageTime());
        dto.setAverageMark(summary.averageScore());
        dto.setHighestMark(summary.highestScore());
        dto.setGoal("");

        // Convert results to DoneResult list
        List<DoneResult> doneResults = results.stream()
                .map(this::convertToDoneResult)
                .collect(Collectors.toList());
        dto.setResults(doneResults);

        return dto;
    }

    private DoneResult convertToDoneResult(Result result) {
        Quiz quiz = Optional.ofNullable(result.getQuiz())
                .orElseThrow(() -> new ObjectNotFound("Quiz is null"));

        DoneResult doneResult = new DoneResult();
        doneResult.setSubmittedDate(result.getSubmittedAt());
        doneResult.setQuizTitle(quiz.getTitle());
        doneResult.setSubject(quiz.getSubjectName());
        doneResult.setFinalMark(result.getScore());
        doneResult.setSpentTime(result.getDuration());

        return doneResult;
    }

    // Record để lưu trữ kết quả tính toán
    private record StatisticsSummary(
            int totalQuizzes,
            long totalDuration,
            double accuracy,
            double averageTime,
            double averageScore,
            double highestScore) {
    }
}
