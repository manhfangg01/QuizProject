package com.quiz.learning.Demo.service.admin;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Answer;
import com.quiz.learning.Demo.domain.Option;
import com.quiz.learning.Demo.domain.Question;
import com.quiz.learning.Demo.domain.filterCriteria.OptionFilter;
import com.quiz.learning.Demo.domain.metadata.Metadata;
import com.quiz.learning.Demo.domain.request.admin.option.CreateOptionRequest;
import com.quiz.learning.Demo.domain.request.admin.option.UpdateOptionRequest;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchOptionDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchOptionPaginationDTO;
import com.quiz.learning.Demo.repository.OptionRepository;
import com.quiz.learning.Demo.service.specification.OptionSpecs;
import com.quiz.learning.Demo.util.error.DuplicatedObjectException;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

@Service
public class AdminOptionService {

    private final JwtEncoder jwtEncoder;

    private final SecurityFilterChain filterChain;
    private final OptionRepository optionRepository;
    private final AdminAnswerService adminAnswerService;
    private final OptionSpecs optionSpecs;

    public AdminOptionService(OptionRepository optionRepository, AdminAnswerService adminAnswerService,
            OptionSpecs optionSpecs, SecurityFilterChain filterChain, JwtEncoder jwtEncoder) {
        this.optionRepository = optionRepository;
        this.adminAnswerService = adminAnswerService;
        this.optionSpecs = optionSpecs;
        this.filterChain = filterChain;
        this.jwtEncoder = jwtEncoder;

    }

    public FetchOptionPaginationDTO handleFetchAllAvailableOptions(int page, int size, String sortBy, String order,
            OptionFilter filter) {
        FetchOptionPaginationDTO dto = new FetchOptionPaginationDTO();
        Pageable pageable = this.handlePagination(page, size, sortBy, order);

        // Thêm điều kiện lọc option chưa gán cho question nào
        Specification<Option> spec = (root, criteria, cb) -> cb.isNull(root.get("question"));

        // Kết hợp với các filter khác
        spec = spec.and(this.optionSpecs.hasId(filter.getId()));
        spec = spec.and(this.optionSpecs.isCorrect(filter.getIsCorrect()));
        spec = spec.and(this.optionSpecs.contextContains(filter.getContext()));

        // Không cần filter questionId vì chúng ta đang tìm options chưa gán
        Page<Option> pageOptions = this.optionRepository.findAll(spec, pageable);

        dto.setOptions(pageOptions.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));

        Metadata metadata = new Metadata();
        metadata.setCurrentPage(page);
        metadata.setPageSize(size);
        metadata.setTotalObjects(pageOptions.getTotalElements());
        metadata.setTotalPages(pageOptions.getTotalPages());
        metadata.setHasNext(pageOptions.hasNext());
        metadata.setHasPrevious(pageOptions.hasPrevious());
        dto.setMetadata(metadata);

        return dto;
    }

    public Option handleGetOption(Long id) {
        Optional<Option> checkOption = this.optionRepository.findById(id);
        if (checkOption.isEmpty()) {
            throw new ObjectNotFound("Option with id: " + id + " is not existed");
        }
        return checkOption.get();
    }

    public List<FetchOptionDTO> handleGetOptions(List<Long> ids) {
        List<Option> options = this.optionRepository.findAllById(ids);
        return options.stream().map(this::convertToDTO).collect(Collectors.toList());

    }

    public FetchAdminDTO.FetchOptionDTO convertToDTO(Option option) {
        FetchAdminDTO.FetchOptionDTO dto = new FetchAdminDTO.FetchOptionDTO();
        dto.setContext(option.getContext());
        dto.setId(option.getId());
        dto.setIsCorrect(option.getIsCorrect());
        dto.setQuestionId(option.getQuestion() == null ? null : option.getQuestion().getId());
        return dto;
    }

    public Pageable handlePagination(int page, int size, String sortBy, String order) {
        // Validate page and size
        page = page < 1 ? 1 : page;
        size = size < 1 ? 10 : size;
        size = size > 100 ? 100 : size; // Giới hạn tối đa 100 items/page

        Sort sort = order.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(page - 1, size, sort);
    }

    public FetchOptionPaginationDTO handleFetchAllOptions(int page, int size, String sortBy, String order,
            OptionFilter filter) {
        FetchOptionPaginationDTO dto = new FetchOptionPaginationDTO();
        Pageable pageable = this.handlePagination(page, size, sortBy, order);
        Specification<Option> spec = (root, criteria, cb) -> cb.conjunction();
        spec = spec.and(this.optionSpecs.hasId(filter.getId()));
        spec = spec.and(this.optionSpecs.hasQuestionId(filter.getQuestionId()));
        spec = spec.and(this.optionSpecs.isCorrect(filter.getIsCorrect()));
        spec = spec.and(this.optionSpecs.contextContains(filter.getContext()));
        Page<Option> pageOptions = this.optionRepository.findAll(spec, pageable);
        List<Option> options = pageOptions.getContent();
        dto.setOptions(options.stream().map(this::convertToDTO).collect(Collectors.toList()));
        Metadata metadata = new Metadata();
        metadata.setCurrentPage(page);
        metadata.setPageSize(size);
        metadata.setTotalObjects(pageOptions.getTotalElements());
        metadata.setTotalPages(pageOptions.getTotalPages());
        metadata.setHasNext(pageOptions.hasNext());
        metadata.setHasPrevious(pageOptions.hasPrevious());
        dto.setMetadata(metadata);
        return dto;

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
