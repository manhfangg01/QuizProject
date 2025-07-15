package com.quiz.learning.Demo.service.client;

import java.util.Collections;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Question;
import com.quiz.learning.Demo.domain.response.client.FetchClientDTO.QuestionClientPlayDTO;

@Service
public class ClientQuestionService {
    private final ClientOptionService clientOptionService;

    public ClientQuestionService(ClientOptionService clientOptionService) {
        this.clientOptionService = clientOptionService;
    }

    public QuestionClientPlayDTO convertToDto(Question question) {
        if (question == null) {
            return null;
        }
        QuestionClientPlayDTO dto = new QuestionClientPlayDTO();
        dto.setContext(question.getContext());
        dto.setOptions(question.getOptions() == null ? Collections.emptyList()
                : question.getOptions().stream().map(clientOptionService::convertToDto).toList());
        dto.setQuestionId(question.getId());
        return dto;
    }

}
