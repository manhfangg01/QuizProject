package com.quiz.learning.Demo.service.admin;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Option;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.repository.OptionRepository;

@Service
public class AdminOptionService {
    private final OptionRepository optionRepository;
    private final AdminAnswerService adminAnswerService;

    public AdminOptionService(OptionRepository optionRepository, AdminAnswerService adminAnswerService) {
        this.optionRepository = optionRepository;
        this.adminAnswerService = adminAnswerService;
    }

    public FetchAdminDTO.FetchOptionDTO convertToDTO(Option option) {
        FetchAdminDTO.FetchOptionDTO dto = new FetchAdminDTO.FetchOptionDTO();
        dto.setContext(option.getContext());
        dto.setId(option.getId());
        // Convert answers nếu có DTO cho nó

        if (option.getAnswers() == null) {
            dto.setAnswers(Collections.emptyList());
        } else {
            List<FetchAdminDTO.FetchAnswerDTO> answerDTOs = option.getAnswers()
                    .stream()
                    .map(adminAnswerService::convertToDTO)
                    .collect(Collectors.toList());
            dto.setAnswers(answerDTOs);
        }
        return dto;
    }

}
