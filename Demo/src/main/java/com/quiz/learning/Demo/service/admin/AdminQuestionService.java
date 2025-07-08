package com.quiz.learning.Demo.service.admin;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Option;
import com.quiz.learning.Demo.domain.Question;
import com.quiz.learning.Demo.domain.filterCriteria.QuestionFilter;
import com.quiz.learning.Demo.domain.metadata.Metadata;
import com.quiz.learning.Demo.domain.request.admin.question.CreateQuestionRequest;
import com.quiz.learning.Demo.domain.request.admin.question.UpdateQuestionRequest;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchQuestionPaginationDTO;
import com.quiz.learning.Demo.repository.QuestionRepository;
import com.quiz.learning.Demo.service.specification.QuestionSpecs;
import com.quiz.learning.Demo.util.error.DuplicatedObjectException;
import com.quiz.learning.Demo.util.error.NullObjectException;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

@Service
public class AdminQuestionService {
    private final QuestionRepository questionRepository;
    private final AdminOptionService adminOptionService;
    private final QuestionSpecs questionSpecs;

    public AdminQuestionService(QuestionRepository questionRepository, AdminOptionService adminOptionService,
            QuestionSpecs questionSpecs) {
        this.questionRepository = questionRepository;
        this.adminOptionService = adminOptionService;
        this.questionSpecs = questionSpecs;
    }

    public Question handleGetQuestion(Long id) {
        Optional<Question> checkQuestion = this.questionRepository.findById(id);
        if (checkQuestion.isEmpty()) {
            throw new ObjectNotFound("Question with id: " + id + " is not existed");
        }
        return checkQuestion.get();
    }

    public FetchAdminDTO.FetchQuestionDTO convertToDTO(Question question) {
        FetchAdminDTO.FetchQuestionDTO dto = new FetchAdminDTO.FetchQuestionDTO();
        dto.setQuestionId(question.getId());
        dto.setContext(question.getContext());
        List<Option> options = question.getOptions();

        List<FetchAdminDTO.FetchOptionDTO> optionDTOs = options == null ? Collections.emptyList()
                : options
                        .stream()
                        .map(adminOptionService::convertToDTO)
                        .collect(Collectors.toList());
        dto.setOptions(optionDTOs);

        return dto;
    }

    public Pageable handlePagination(int page, int size, String sortBy, String order) {
        page = page < 1 ? 1 : page;
        size = size < 1 ? 10 : size;
        size = size > 100 ? 100 : size;
        Sort sort = order.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(page - 1, size, sort);
    }

    public FetchQuestionPaginationDTO handleFetchAllQuestions(int page, int size, String sortBy, String order,
            QuestionFilter filter) {
        FetchQuestionPaginationDTO dto = new FetchQuestionPaginationDTO();
        Pageable pageable = this.handlePagination(page, size, sortBy, order);
        Specification<Question> spec = (root, criteria, cb) -> cb.conjunction();
        spec = spec.and(this.questionSpecs.contextContains(filter.getContext()));
        spec = spec.and(this.questionSpecs.hasAtLeastNOptions(filter.getNumberOfOptions()));
        spec = spec.and(this.questionSpecs.hasId(filter.getId()));
        spec = spec.and(this.questionSpecs.hasQuizId(filter.getQuizId()));
        Page<Question> pageQuestions = this.questionRepository.findAll(spec, pageable);
        List<Question> questions = pageQuestions.getContent();
        dto.setQuestions(questions.stream().map(this::convertToDTO).collect(Collectors.toList()));
        Metadata metadata = new Metadata();
        metadata.setCurrentPage(page);
        metadata.setPageSize(size);
        metadata.setTotalObjects(pageQuestions.getTotalElements());
        metadata.setTotalPages(pageQuestions.getTotalPages());
        metadata.setHasNext(pageQuestions.hasNext());
        metadata.setHasPrevious(pageQuestions.hasPrevious());
        dto.setMetadata(metadata);

        return dto;
    }

    public FetchAdminDTO.FetchQuestionDTO handleFetchOneQuestion(Long id) {
        Optional<Question> checkQuestion = this.questionRepository.findById(id);
        if (checkQuestion.isEmpty()) {
            throw new ObjectNotFound("Question not found");
        }
        return convertToDTO(checkQuestion.get());
    }

    private List<Option> fetchOptionsByIds(List<Long> ids) {
        return ids == null ? Collections.emptyList()
                : ids
                        .stream()
                        .map(id -> {
                            return adminOptionService.handleGetOption(id);
                        })
                        .collect(Collectors.toList());
    }

    public FetchAdminDTO.FetchQuestionDTO handleCreateQuestion(CreateQuestionRequest newQuestion) {

        if (newQuestion == null) {
            throw new NullObjectException("New Question is Null");
        }

        if (this.questionRepository.findByContext(newQuestion.getContext()).isPresent()) {
            throw new DuplicatedObjectException("Question with similar context is found");
        }

        // Tạo question
        Question question = new Question();
        question.setContext(newQuestion.getContext());

        // Map từ CreateOptionRequest -> Option
        question.setOptions(this.fetchOptionsByIds(newQuestion.getOptionIds()));
        // Lưu và trả về DTO
        Question saved = questionRepository.save(question);
        return convertToDTO(saved);
    }

    public FetchAdminDTO.FetchQuestionDTO handleUpdateQuestion(UpdateQuestionRequest updatedQuestion) {
        Optional<Question> checkQuestion = this.questionRepository.findById(updatedQuestion.getQuestionId());
        if (checkQuestion.isEmpty()) {
            throw new ObjectNotFound("Question not found");
        }
        Question realQuestion = checkQuestion.get();
        if (this.questionRepository.findByContext(updatedQuestion.getContext()).isPresent()) {
            if (updatedQuestion.getContext().equalsIgnoreCase(realQuestion.getContext()))
                throw new DuplicatedObjectException("Question with the similar context is found");
        }
        Question question = new Question();
        question.setContext(updatedQuestion.getContext());

        question.setOptions(this.fetchOptionsByIds(updatedQuestion.getOptionIds()));
        // Lưu và trả về DTO
        Question saved = questionRepository.save(question);
        return convertToDTO(saved);
    }

}
