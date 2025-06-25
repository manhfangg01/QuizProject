package com.quiz.learning.Demo.service.admin;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Answer;

import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.repository.AnswerRepository;

@Service
public class AdminAnswerService {
    private final AnswerRepository answerRepository;

    public AdminAnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public FetchAdminDTO.FetchAnswerDTO convertToDTO(Answer answer) {
        FetchAdminDTO.FetchAnswerDTO dto = new FetchAdminDTO.FetchAnswerDTO();
        dto.setId(answer.getId());
        dto.setCorrect(answer.isCorrect()); // hoặc answer.getIsCorrect() tùy vào
        dto.setOptionId(answer.getSelectedOption().getId());
        return dto;
    }

}
