package com.quiz.learning.Demo.service.admin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Result;
import com.quiz.learning.Demo.domain.User;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.repository.ResultRepository;
import com.quiz.learning.Demo.repository.UserRepository;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

@Service
public class AdminResultService {
    private final ResultRepository resultRepository;
    private final UserRepository userRepository;

    public AdminResultService(ResultRepository resultRepository, UserRepository userRepository) {
        this.resultRepository = resultRepository;
        this.userRepository = userRepository;
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

    public FetchAdminDTO.FetchResultDTO handleFetchOneResult(Long id) {
        Optional<Result> checkResult = this.resultRepository.findById(id);
        if (checkResult.isEmpty()) {
            throw new ObjectNotFound("There is no result has id " + id);
        }
        return this.convertToDTO(checkResult.get());
    }

    public List<FetchAdminDTO.FetchResultDTO> handleFetchResultByUserId(Long userId) {
        Optional<User> checkUser = this.userRepository.findById(userId);
        if (checkUser.isEmpty()) {
            throw new ObjectNotFound("There is no user has id " + userId);
        }
        User realUser = checkUser.get();
        List<Result> results = realUser.getResults();

        return results == null ? null : results.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

}
