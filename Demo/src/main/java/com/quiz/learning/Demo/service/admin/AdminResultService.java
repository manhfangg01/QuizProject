package com.quiz.learning.Demo.service.admin;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Quiz;
import com.quiz.learning.Demo.domain.Result;
import com.quiz.learning.Demo.domain.User;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.repository.QuizRepository;
import com.quiz.learning.Demo.repository.ResultRepository;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

@Service
public class AdminResultService {
    private final ResultRepository resultRepository;
    private final AdminUserService adminUserService;
    private final QuizRepository quizRepository;
    private final AdminAnswerService adminAnswerService;

    public AdminResultService(ResultRepository resultRepository, AdminUserService adminUserService,
            QuizRepository quizRepository, AdminAnswerService adminAnswerService) {
        this.resultRepository = resultRepository;
        this.quizRepository = quizRepository;
        this.adminUserService = adminUserService;
        this.adminAnswerService = adminAnswerService;
    }

    public Result handleGetResult(Long id) {
        Optional<Result> checkResult = this.resultRepository.findById(id);
        if (checkResult.isEmpty()) {
            throw new ObjectNotFound("Result with id: " + id + " is not existed");
        }
        return checkResult.get();
    }

    public FetchAdminDTO.FetchResultDTO convertToDTO(Result result) {
        FetchAdminDTO.FetchResultDTO dto = new FetchAdminDTO.FetchResultDTO();
        dto.setId(result.getId());
        dto.setScore(result.getScore());
        dto.setSubmittedAt(result.getSubmittedAt());
        dto.setUserId(result.getUser() == null ? null : result.getUser().getId());
        dto.setQuizId(result.getQuiz() == null ? null : result.getQuiz().getId());
        dto.setAnswers(result.getAnswers() == null ? Collections.emptyList()
                : result.getAnswers().stream().map(adminAnswerService::convertToDTO).collect(Collectors.toList()));
        return dto;
    }

    public List<FetchAdminDTO.FetchResultDTO> handleFetchAllResults() {
        List<Result> results = this.resultRepository.findAll();
        return results == null ? Collections.emptyList()
                : results
                        .stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
    }

    public List<FetchAdminDTO.FetchResultDTO> handleFetchResultsByQuizId(Long id) {
        Optional<Quiz> checkQuiz = this.quizRepository.findById(id);
        if (checkQuiz.isEmpty()) {
            throw new ObjectNotFound("Quiz with id: " + id + " is not existed");
        }
        Quiz realQuiz = checkQuiz.get();
        List<Result> results = realQuiz.getResults();

        return results == null ? Collections.emptyList()
                : results.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<FetchAdminDTO.FetchResultDTO> handleFetchResultsByUserId(Long userId) {

        User realUser = this.adminUserService.handleGetUser(userId);
        List<Result> results = realUser.getResults();

        return results == null ? Collections.emptyList()
                : results.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public void handleDeleteResult(Result result) {
        this.resultRepository.delete(result);
    }

}
