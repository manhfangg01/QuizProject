package com.quiz.learning.Demo.service.admin;

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
import com.quiz.learning.Demo.repository.UserRepository;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

@Service
public class AdminResultService {
    private final ResultRepository resultRepository;
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;

    public AdminResultService(ResultRepository resultRepository, UserRepository userRepository,
            QuizRepository quizRepository) {
        this.resultRepository = resultRepository;
        this.userRepository = userRepository;
        this.quizRepository = quizRepository;
    }

    public FetchAdminDTO.FetchResultDTO convertToDTO(Result result) {
        FetchAdminDTO.FetchResultDTO dto = new FetchAdminDTO.FetchResultDTO();
        dto.setId(result.getId());
        dto.setScore(result.getScore());
        dto.setSubmittedAt(result.getSubmittedAt());
        // dto.setUserId(result.getUser());
        return dto;
    }

    public List<FetchAdminDTO.FetchResultDTO> handleFetchAllResults() {
        return this.resultRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<FetchAdminDTO.FetchResultDTO> handleFetchResultsByQuizId(Long id) {
        Optional<Quiz> checkQuiz = this.quizRepository.findById(id);
        if (checkQuiz.isEmpty()) {
            throw new ObjectNotFound("There is no quiz has id " + id);
        }
        Quiz realQuiz = checkQuiz.get();
        List<Result> results = realQuiz.getResults();

        return results == null ? null : results.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<FetchAdminDTO.FetchResultDTO> handleFetchResultsByUserId(Long userId) {
        Optional<User> checkUser = this.userRepository.findById(userId);
        if (checkUser.isEmpty()) {
            throw new ObjectNotFound("There is no user has id " + userId);
        }
        User realUser = checkUser.get();
        List<Result> results = realUser.getResults();

        return results == null ? null : results.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

}
