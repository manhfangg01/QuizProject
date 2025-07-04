package com.quiz.learning.Demo.service.admin;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Answer;
import com.quiz.learning.Demo.domain.Option;
import com.quiz.learning.Demo.domain.Question;
import com.quiz.learning.Demo.domain.request.admin.option.CreateOptionRequest;
import com.quiz.learning.Demo.domain.request.admin.option.UpdateOptionRequest;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.repository.OptionRepository;
import com.quiz.learning.Demo.util.error.DuplicatedObjectException;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

@Service
public class AdminOptionService {
    private final OptionRepository optionRepository;
    private final AdminAnswerService adminAnswerService;

    public AdminOptionService(OptionRepository optionRepository, AdminAnswerService adminAnswerService) {
        this.optionRepository = optionRepository;
        this.adminAnswerService = adminAnswerService;

    }

    public Option handleGetOption(Long id) {
        Optional<Option> checkOption = this.optionRepository.findById(id);
        if (checkOption.isEmpty()) {
            throw new ObjectNotFound("Option with id: " + id + " is not existed");
        }
        return checkOption.get();
    }

    public FetchAdminDTO.FetchOptionDTO convertToDTO(Option option) {
        FetchAdminDTO.FetchOptionDTO dto = new FetchAdminDTO.FetchOptionDTO();
        dto.setContext(option.getContext());
        dto.setId(option.getId());
        dto.setIsCorrect(option.getIsCorrect());
        return dto;
    }

    public List<FetchAdminDTO.FetchOptionDTO> handleFetchAllOptions() {
        List<Option> options = this.optionRepository.findAll();

        return options == null ? Collections.emptyList()
                : options
                        .stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());

    }

    public FetchAdminDTO.FetchOptionDTO handleFetchOneOption(Long id) {
        Optional<Option> checkOption = this.optionRepository.findById(id);
        if (checkOption.isEmpty()) {
            throw new ObjectNotFound("There is no option that has id " + id);
        }
        return this.convertToDTO(checkOption.get());
    }

    public FetchAdminDTO.FetchOptionDTO handleCreateOption(CreateOptionRequest newOption) {
        if (this.optionRepository.findByContext(newOption.getContext()).isPresent()) {
            throw new DuplicatedObjectException("There is an option has this context");
        }
        Option option = new Option();
        option.setContext(newOption.getContext());
        option.setIsCorrect(newOption.getIsCorrect());
        return this.convertToDTO(this.optionRepository.save(option));
    }

    public FetchAdminDTO.FetchOptionDTO handleUpdateOption(UpdateOptionRequest updatedOption) {
        Optional<Option> checkOption = this.optionRepository.findById(updatedOption.getOptionId());
        if (checkOption.isEmpty()) {
            throw new ObjectNotFound("There is no option that has id " + updatedOption.getOptionId());
        }
        Option realOption = checkOption.get();
        if (!realOption.getContext().equalsIgnoreCase(updatedOption.getContext())) {
            if (this.optionRepository.findByContext(updatedOption.getContext()).isPresent()) {
                throw new DuplicatedObjectException("There is an option has this context");
            }
        }
        realOption.setContext(updatedOption.getContext());
        realOption.setIsCorrect(updatedOption.getIsCorrect());
        return this.convertToDTO(this.optionRepository.save(realOption));

    }

    public void handleDeleteOption(Long id) {
        Optional<Option> checkOption = this.optionRepository.findById(id);
        if (checkOption.isEmpty()) {
            throw new ObjectNotFound("There is no option that has id " + id);
        }
        Option realOption = checkOption.get();
        Question question = realOption.getQuestion();
        if (question != null) {
            question.getOptions().remove(realOption);
        }
        if (realOption.getAnswers() != null) {
            for (Answer ans : realOption.getAnswers()) {
                this.adminAnswerService.handleDeleteAnswer(ans);
            }
        }
        optionRepository.delete(realOption);

        optionRepository.delete(realOption);
    }

}
