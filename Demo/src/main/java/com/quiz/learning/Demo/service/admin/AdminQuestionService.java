package com.quiz.learning.Demo.service.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.web.multipart.MultipartFile;

import com.quiz.learning.Demo.domain.Option;
import com.quiz.learning.Demo.domain.Question;
import com.quiz.learning.Demo.domain.User;
import com.quiz.learning.Demo.domain.filterCriteria.admin.QuestionFilter;
import com.quiz.learning.Demo.domain.metadata.Metadata;
import com.quiz.learning.Demo.domain.request.admin.question.CreateQuestionRequest;
import com.quiz.learning.Demo.domain.request.admin.question.UpdateQuestionRequest;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchQuestionPaginationDTO;
import com.quiz.learning.Demo.repository.OptionRepository;
import com.quiz.learning.Demo.repository.QuestionRepository;
import com.quiz.learning.Demo.service.azure.AzureBlobService;
import com.quiz.learning.Demo.service.specification.QuestionSpecs;
import com.quiz.learning.Demo.util.error.DuplicatedObjectException;
import com.quiz.learning.Demo.util.error.InvalidUploadedFile;
import com.quiz.learning.Demo.util.error.NullObjectException;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

@Service
public class AdminQuestionService {
    private final QuestionRepository questionRepository;
    private final AdminOptionService adminOptionService;
    private final QuestionSpecs questionSpecs;
    private final OptionRepository optionRepository;
    private final AzureBlobService azureBlobService;

    public AdminQuestionService(QuestionRepository questionRepository, AdminOptionService adminOptionService,
            QuestionSpecs questionSpecs, OptionRepository optionRepository, AzureBlobService azureBlobService) {
        this.questionRepository = questionRepository;
        this.adminOptionService = adminOptionService;
        this.questionSpecs = questionSpecs;
        this.optionRepository = optionRepository;
        this.azureBlobService = azureBlobService;
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
        dto.setQuestionImage(question.getQuestionImage());

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

    public void handleAssignQuestionImage(Question question, MultipartFile image) {
        try {
            if (image != null && !image.isEmpty()) {

                List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx", "webp");
                String fileName = image.getOriginalFilename().toLowerCase();
                String contentType = image.getContentType();
                boolean validMine = contentType != null && contentType.startsWith("image/");
                boolean validExt = allowedExtensions.stream().anyMatch(fileName::endsWith);
                if (!validMine || !validExt) {
                    throw new InvalidUploadedFile(
                            "Invalid file type. Only allows: " + allowedExtensions.toString());
                }
                String imageUrl = azureBlobService.uploadFile(image);
                question.setQuestionImage(imageUrl);
            } else {
                question.setQuestionImage("");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload avatar", e);
        }

    }

    public void handleAssignQuestionAudio(Question question, MultipartFile audio) {
        try {
            if (audio != null && !audio.isEmpty()) {
                List<String> allowedExtensions = List.of(".mp3", ".wav", ".ogg", ".m4a");

                String fileName = audio.getOriginalFilename().toLowerCase();
                String contentType = audio.getContentType();
                // boolean validMime = contentType != null && contentType.startsWith("audio/");
                // // Kiểm tra định dạng MINE
                boolean validExt = allowedExtensions.stream().anyMatch(fileName::endsWith); // Kiểm tra phần mở rộng
                if (!validExt) {
                    throw new InvalidUploadedFile(
                            "Invalid file type. Only allows: " + allowedExtensions.toString());
                }

                String audioUrl = azureBlobService.uploadFile(audio);
                question.setQuestionAudio(audioUrl);
            } else {
                question.setQuestionAudio("");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload audio", e);
        }
    }

    public FetchAdminDTO.FetchQuestionDTO handleCreateQuestion(CreateQuestionRequest newQuestion,
            MultipartFile questionImage, MultipartFile questionAudio) {

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
        List<Option> options = this.fetchOptionsByIds(newQuestion.getOptionIds()); // Lưu và trả về DTO

        // Thiết lập quan hệ 2 chiều
        for (Option opt : options) {
            opt.setQuestion(question); // Quan trọng: gán ngược lại
        }
        question.setOptions(options);
        this.handleAssignQuestionImage(question, questionImage);
        this.handleAssignQuestionAudio(question, questionAudio);

        Question saved = questionRepository.save(question);
        return convertToDTO(saved);
    }

    public FetchAdminDTO.FetchQuestionDTO handleUpdateQuestion(UpdateQuestionRequest updatedQuestion,
            MultipartFile questionImage) {
        Optional<Question> checkQuestion = this.questionRepository.findById(updatedQuestion.getQuestionId());
        if (checkQuestion.isEmpty()) {
            throw new ObjectNotFound("Question not found");
        }

        Question realQuestion = checkQuestion.get();

        // Kiểm tra trùng context
        Optional<Question> duplicate = questionRepository.findByContext(updatedQuestion.getContext());
        if (duplicate.isPresent() && !duplicate.get().getId().equals(realQuestion.getId())) {
            throw new DuplicatedObjectException("Question with the similar context is found");
        }

        // Cập nhật context
        realQuestion.setContext(updatedQuestion.getContext());

        // Lấy danh sách option mới từ DB
        List<Option> newOptions = optionRepository.findAllById(updatedQuestion.getOptionIds());

        // Gỡ liên kết các option bị bỏ chọn
        List<Option> oldOptions = new ArrayList<>(realQuestion.getOptions());
        for (Option oldOption : oldOptions) {
            if (!updatedQuestion.getOptionIds().contains(oldOption.getId())) {
                oldOption.setQuestion(null); // gỡ liên kết
            }
        }

        // Gán lại danh sách mới
        for (Option newOption : newOptions) {
            newOption.setQuestion(realQuestion); // đảm bảo liên kết
        }
        realQuestion.setOptions(newOptions);
        if (questionImage != null) {
            try {

                if (questionImage != null && !questionImage.isEmpty()) {
                    String imageUrl = azureBlobService.uploadFile(questionImage);
                    realQuestion.setQuestionImage(imageUrl);
                }

            } catch (IOException e) {
                throw new RuntimeException("Failed to upload avatar", e);
            }
        }

        // Lưu lại
        Question saved = questionRepository.save(realQuestion);
        return convertToDTO(saved);
    }

}
