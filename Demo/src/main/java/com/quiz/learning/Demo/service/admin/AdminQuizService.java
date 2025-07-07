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

import com.quiz.learning.Demo.domain.Question;
import com.quiz.learning.Demo.domain.Quiz;
import com.quiz.learning.Demo.domain.User;
import com.quiz.learning.Demo.domain.filterCriteria.QuizFilter;
import com.quiz.learning.Demo.domain.filterCriteria.UserFilter;
import com.quiz.learning.Demo.domain.metadata.Metadata;
import com.quiz.learning.Demo.domain.request.admin.quiz.CreateQuizRequest;
import com.quiz.learning.Demo.domain.request.admin.quiz.UpdateQuizRequest;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchQuizPaginationDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchTableQuizDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchUserPaginationDTO;
import com.quiz.learning.Demo.repository.QuizRepository;
import com.quiz.learning.Demo.service.CalculationFunction;
import com.quiz.learning.Demo.service.specification.QuizSpecs;
import com.quiz.learning.Demo.util.error.DuplicatedObjectException;
import com.quiz.learning.Demo.util.error.NullObjectException;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

@Service
public class AdminQuizService {
    private final QuizRepository quizRepository;
    private final AdminQuestionService adminQuestionService;
    private final AdminResultService adminResultService;
    private final QuizSpecs quizSpecs;

    public AdminQuizService(QuizRepository quizRepository, AdminQuestionService adminQuestionService,
            AdminResultService adminResultService, QuizSpecs quizSpecs) {
        this.quizRepository = quizRepository;
        this.adminQuestionService = adminQuestionService;
        this.adminResultService = adminResultService;
        this.quizSpecs = quizSpecs;
    }

    public Quiz handleGetQuiz(Long id) {
        Optional<Quiz> checkQuiz = this.quizRepository.findById(id);
        if (checkQuiz.isEmpty()) {
            throw new ObjectNotFound("Quiz with id: " + id + " is not existed");
        }
        return checkQuiz.get();
    }

    public void handleSaveQuiz(Quiz quiz) {
        this.quizRepository.save(quiz);
    }

    public FetchAdminDTO.FetchFullQuizDTO convertToFullDTO(Quiz quiz) {
        FetchAdminDTO.FetchFullQuizDTO dto = new FetchAdminDTO.FetchFullQuizDTO();
        dto.setQuizId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        dto.setSubjectName(quiz.getSubjectName());
        dto.setTimeLimit(quiz.getTimeLimit());
        dto.setTotalParticipants(quiz.getTotalParticipants());
        dto.setIsActive(quiz.getIsActive());
        dto.setDifficulty(quiz.getDifficulty());

        // ✅ convert list question

        List<FetchAdminDTO.FetchQuestionDTO> questionDTOs = quiz.getQuestions() == null ? Collections.emptyList()
                : quiz
                        .getQuestions()
                        .stream()
                        .map(adminQuestionService::convertToDTO)
                        .collect(Collectors.toList());
        dto.setQuestions(questionDTOs);

        if (quiz.getResults() != null) {
            List<FetchAdminDTO.FetchResultDTO> resultDTOs = quiz.getResults() == null ? Collections.emptyList()
                    : quiz
                            .getResults()
                            .stream()
                            .map(adminResultService::convertToDTO)
                            .collect(Collectors.toList());
            dto.setResults(resultDTOs);
        }

        return dto;
    }

    public FetchTableQuizDTO convertToTableDTO(Quiz quiz) {
        FetchTableQuizDTO dto = new FetchTableQuizDTO();
        dto.setDifficulty(quiz.getDifficulty());
        dto.setIsActive(quiz.getIsActive());
        dto.setQuizId(quiz.getId());
        dto.setSubjectName(quiz.getSubjectName());
        dto.setTimeLimit(quiz.getTimeLimit());
        dto.setTitle(quiz.getTitle());
        dto.setTotalParticipants(quiz.getTotalParticipants());
        return dto;
    }

    public Pageable handlePagination(int page, int size, String sortBy, String order) {
        // Validate input parameters
        page = Math.max(page, 1); // Đảm bảo page >= 1
        size = Math.max(size, 1); // Đảm bảo size >= 1

        Sort sort = order.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(page - 1, size, sort);
    }

    public FetchQuizPaginationDTO handleFetchAllQuizzes(int page, int size, String sortBy, String order,
            QuizFilter filterCriteria) {
        Pageable pageable = this.handlePagination(page, size, sortBy, order);

        // Xây dựng specification
        Specification<Quiz> spec = Specification.where(null);

        if (filterCriteria.getId() != null) {
            spec = spec.and(this.quizSpecs.hasId(filterCriteria.getId()));
        }
        if (filterCriteria.getTitle() != null) {
            spec = spec.and(this.quizSpecs.titleContains(filterCriteria.getTitle()));
        }
        if (filterCriteria.getSubject() != null) {
            spec = spec.and(this.quizSpecs.hasSubject(filterCriteria.getSubject()));
        }
        // Thêm các điều kiện filter khác tương tự...

        Page<Quiz> pageQuizzes = this.quizRepository.findAll(spec, pageable);
        List<Quiz> quizzes = pageQuizzes.getContent();

        // Gán DTO
        FetchQuizPaginationDTO dto = new FetchQuizPaginationDTO();
        Metadata metadata = new Metadata();
        metadata.setCurrentPage(page);
        metadata.setPageSize(size);
        metadata.setTotalObjects(pageQuizzes.getTotalElements());
        metadata.setTotalPages(pageQuizzes.getTotalPages()); // Sửa: bỏ -1
        metadata.setHasNext(pageQuizzes.hasNext()); // Sử dụng method có sẵn
        metadata.setHasPrevious(pageQuizzes.hasPrevious()); // Sử dụng method có sẵn

        dto.setMetadata(metadata);
        dto.setQuizzes(quizzes.stream().map(this::convertToTableDTO).collect(Collectors.toList()));
        return dto;
    }

    public FetchAdminDTO.FetchFullQuizDTO handleFetchQuizById(Long id) {
        Optional<Quiz> checkQuiz = this.quizRepository.findById(id);
        if (checkQuiz.isEmpty()) {
            throw new ObjectNotFound("Quiz Not Found");
        }
        return convertToFullDTO(checkQuiz.get());
    }

    private List<Question> fetchQuestionsByIds(List<Long> ids) {
        return ids == null ? Collections.emptyList()
                : ids
                        .stream()
                        .map(id -> {
                            return this.adminQuestionService.handleGetQuestion(id);
                        })
                        .collect(Collectors.toList());
    }

    public FetchAdminDTO.FetchTableQuizDTO handleCreateQuiz(CreateQuizRequest createdQuiz) {
        if (createdQuiz == null) {
            throw new NullObjectException("Quiz is null");
        }

        if (quizRepository.findByTitle(createdQuiz.getTitle()).isPresent()) {
            throw new DuplicatedObjectException("Quiz's title is duplicated");
        }

        if (createdQuiz.getQuestions() == null || createdQuiz.getQuestions().isEmpty()) {
            throw new NullObjectException("Quiz must contain at least one question");
        }

        Quiz quiz = new Quiz();
        quiz.setIsActive(createdQuiz.getIsActive());
        quiz.setDifficulty(createdQuiz.getDifficulty());
        quiz.setQuestions(fetchQuestionsByIds(createdQuiz.getQuestions()));
        quiz.setSubjectName(createdQuiz.getSubjectName());
        quiz.setTimeLimit(createdQuiz.getTimeLimit());
        quiz.setTitle(createdQuiz.getTitle());
        quiz.setTotalParticipants(Long.valueOf(0));

        Quiz saved = quizRepository.save(quiz);
        return convertToTableDTO(saved);
    }

    public FetchAdminDTO.FetchTableQuizDTO handleUpdateQuiz(UpdateQuizRequest request) {
        Quiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() -> new ObjectNotFound("Quiz with id " + request.getQuizId() + " not found"));

        // Check trùng title (nếu title bị đổi)
        if (!quiz.getTitle().equals(request.getTitle())
                && quizRepository.findByTitle(request.getTitle()).isPresent()) {
            throw new DuplicatedObjectException("Title is duplicated");
        }

        quiz.setTitle(request.getTitle());
        quiz.setSubjectName(request.getSubjectName());
        quiz.setTimeLimit(request.getTimeLimit());
        quiz.setIsActive(request.getIsActive());
        quiz.setDifficulty(request.getDifficulty());

        // Lấy danh sách câu hỏi mới
        List<Question> updatedQuestions = fetchQuestionsByIds(request.getQuestions());
        quiz.setQuestions(updatedQuestions);

        return convertToTableDTO(quizRepository.save(quiz));
    }

    public void handleDeleteQuiz(Long id) {
        Optional<Quiz> checkQuiz = this.quizRepository.findById(id);
        if (checkQuiz.isEmpty()) {
            throw new ObjectNotFound("Quiz Not Found");
        }
        this.quizRepository.delete(checkQuiz.get());
    }

}
