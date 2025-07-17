package com.quiz.learning.Demo.service.client;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Option;
import com.quiz.learning.Demo.domain.response.client.FetchClientDTO.OptionClientPlayDTO;
import com.quiz.learning.Demo.domain.response.client.result.ClientDetailResultDTO.DetailAnswer;
import com.quiz.learning.Demo.domain.response.client.result.ClientDetailResultDTO.DetailOption;

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

    public DetailOption convertToDetailDto(Option option) {
        if (option == null) {
            return null;
        } else {
            DetailOption dto = new DetailOption();
            dto.setIsCorrect(option.getIsCorrect());
            dto.setOptionContext(option.getContext());
            dto.setOptionId(option.getId());
            return dto;
        }
    }

}
