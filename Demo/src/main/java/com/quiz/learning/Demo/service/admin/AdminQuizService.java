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
import com.quiz.learning.Demo.domain.filterCriteria.admin.QuizFilter;
import com.quiz.learning.Demo.domain.metadata.Metadata;
import com.quiz.learning.Demo.domain.request.admin.quiz.CreateQuizRequest;
import com.quiz.learning.Demo.domain.request.admin.quiz.UpdateQuizRequest;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchFullQuizDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchQuestionDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchQuizPaginationDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchResultDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchTableQuizDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.QuizPopularityDTO;
import com.quiz.learning.Demo.repository.QuizRepository;
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

    public List<QuizPopularityDTO> handleGetTopQuizzes(Pageable pageable) {
        return quizRepository.findTopQuizzes(PageRequest.of(0, 5));
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
        FetchFullQuizDTO dto = new FetchFullQuizDTO();
        dto.setQuizId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        dto.setSubjectName(quiz.getSubjectName());
        dto.setTimeLimit(quiz.getTimeLimit());
        dto.setTotalParticipants(quiz.getTotalParticipants());
        dto.setIsActive(quiz.getIsActive());
        dto.setDifficulty(quiz.getDifficulty());

        // 🔥 Quan trọng: map câu hỏi sang FetchQuestionDTO
        List<FetchQuestionDTO> questions = quiz.getQuestions().stream()
                .map(q -> {
                    FetchQuestionDTO qDto = new FetchQuestionDTO();
                    qDto.setQuestionId(q.getId());
                    qDto.setContext(q.getContext());
                    // Nếu cần, map options nữa
                    return qDto;
                }).collect(Collectors.toList());
        dto.setQuestions(questions);

        // 🔥 Quan trọng: map kết quả sang FetchResultDTO nếu cần
        List<FetchResultDTO> results = quiz.getResults().stream()
                .map(r -> {
                    FetchResultDTO rDto = new FetchResultDTO();
                    rDto.setId(r.getId());
                    rDto.setUserId(r.getUser().getId());
                    rDto.setQuizId(quiz.getId());
                    rDto.setScore(r.getScore());
                    rDto.setSubmittedAt(r.getSubmittedAt());
                    return rDto;
                }).collect(Collectors.toList());
        dto.setResults(results);
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
        Specification<Quiz> spec = (root, query, cb) -> cb.conjunction();
        Specification<Quiz> spec1 = this.quizSpecs.hasId(filterCriteria.getId());
        Specification<Quiz> spec2 = this.quizSpecs.titleContains(filterCriteria.getTitle());
        Specification<Quiz> spec3 = this.quizSpecs.hasSubject(filterCriteria.getSubject());
        Specification<Quiz> spec4 = this.quizSpecs.hasDifficulty(filterCriteria.getDifficulty());
        Specification<Quiz> spec5 = this.quizSpecs.isActive(filterCriteria.getActive());
        Specification<Quiz> spec6 = this.quizSpecs.hasMinParticipants(filterCriteria.getTotalParticipants());
        Specification<Quiz> spec7 = this.quizSpecs.timeLimitLessThanOrEqual(filterCriteria.getTimeLimit());
        spec = spec.and(spec1).and(spec2).and(spec3).and(spec4).and(spec5).and(spec6).and(spec7);
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
        dto.setQuizzes(quizzes.stream().map(this::convertToFullDTO).collect(Collectors.toList()));
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
