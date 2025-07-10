package com.quiz.learning.Demo.service.client;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Option;
import com.quiz.learning.Demo.domain.response.client.FetchClientDTO.OptionClientPlayDTO;

@Service
public class ClientOptionService {
    public OptionClientPlayDTO convertToDto(Option option) {
        if (option == null) {
            return null;
        } else {
            OptionClientPlayDTO dto = new OptionClientPlayDTO();
            dto.setContent(option.getContext());
            dto.setOptionId(option.getId());
            return dto;
        }
    }

}
