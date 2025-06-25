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
import com.quiz.learning.Demo.repository.AnswerRepository;
import com.quiz.learning.Demo.repository.OptionRepository;
import com.quiz.learning.Demo.util.error.DuplicatedObjectException;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

@Service
public class AdminOptionService {
    private final OptionRepository optionRepository;
    private final AdminAnswerService adminAnswerService;
    private final AnswerRepository answerRepository;

    public AdminOptionService(OptionRepository optionRepository, AdminAnswerService adminAnswerService,
            AnswerRepository answerRepository) {
        this.optionRepository = optionRepository;
        this.adminAnswerService = adminAnswerService;
        this.answerRepository = answerRepository;
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

    public List<FetchAdminDTO.FetchOptionDTO> handleFetchAllOptions() {
        return this.optionRepository.findAll().stream()
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
        option.setCorrect(newOption.isCorrect());
        return this.convertToDTO(this.optionRepository.save(option));
    }

    public FetchAdminDTO.FetchOptionDTO handleUpdateOption(UpdateOptionRequest updatedOption) {
        Optional<Option> checkOption = this.optionRepository.findById(updatedOption.getId());
        if (checkOption.isEmpty()) {
            throw new ObjectNotFound("There is no option that has id " + updatedOption.getId());
        }
        Option realOption = checkOption.get();
        if (!realOption.getContext().equalsIgnoreCase(updatedOption.getContext())) {
            if (this.optionRepository.findByContext(updatedOption.getContext()).isPresent()) {
                throw new DuplicatedObjectException("There is an option has this context");
            }
        }
        realOption.setContext(updatedOption.getContext());
        realOption.setCorrect(updatedOption.isCorrect());
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

        for (Answer ans : realOption.getAnswers()) {
            this.answerRepository.delete(ans);
        }
        optionRepository.delete(realOption);

        optionRepository.delete(realOption);
    }

}
