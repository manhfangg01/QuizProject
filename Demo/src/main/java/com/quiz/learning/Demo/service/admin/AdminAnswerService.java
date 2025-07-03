package com.quiz.learning.Demo.service.admin;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Answer;

import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.repository.AnswerRepository;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

@Service
public class AdminAnswerService {
    private final AnswerRepository answerRepository;

    public AdminAnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public Answer handleGetAnswer(Long id) {
        Optional<Answer> checkAnswer = this.answerRepository.findById(id);
        if (checkAnswer.isEmpty()) {
            throw new ObjectNotFound("Answer with id: " + id + " is not existed");
        }
        return checkAnswer.get();
    }

    public FetchAdminDTO.FetchAnswerDTO convertToDTO(Answer answer) {
        FetchAdminDTO.FetchAnswerDTO dto = new FetchAdminDTO.FetchAnswerDTO();
        dto.setId(answer.getId());
        dto.setIsCorrect(answer.getIsCorrect()); // hoặc answer.getIsCorrect() tùy vào
        dto.setOptionId(answer.getSelectedOption().getId());
        return dto;
    }

    public void handleDeleteAnswer(Answer answer) {
        this.answerRepository.delete(answer);
    }

}
