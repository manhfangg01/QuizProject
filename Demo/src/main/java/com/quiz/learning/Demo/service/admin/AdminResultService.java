package com.quiz.learning.Demo.service.admin;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Result;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.repository.ResultRepository;

@Service
public class AdminResultService {
    private final ResultRepository resultRepository;

    public AdminResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public FetchAdminDTO.FetchResultDTO convertToDTO(Result result) {
        FetchAdminDTO.FetchResultDTO dto = new FetchAdminDTO.FetchResultDTO();
        dto.setId(result.getId());
        dto.setScore(result.getScore());
        dto.setSubmittedAt(result.getSubmittedAt());
        // dto.setUserId(result.getUser());
        return dto;
    }

}
