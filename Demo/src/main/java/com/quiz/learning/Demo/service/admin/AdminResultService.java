package com.quiz.learning.Demo.service.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Answer;
import com.quiz.learning.Demo.domain.Question;
import com.quiz.learning.Demo.domain.Quiz;
import com.quiz.learning.Demo.domain.Result;
import com.quiz.learning.Demo.domain.User;
import com.quiz.learning.Demo.domain.filterCriteria.admin.ResultFilter;
import com.quiz.learning.Demo.domain.metadata.Metadata;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchResultDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchResultPaginationDTO;
import com.quiz.learning.Demo.domain.response.client.result.ClientDetailResultDTO;
import com.quiz.learning.Demo.domain.response.client.result.ClientDetailResultDTO.DetailAnswer;
import com.quiz.learning.Demo.domain.response.client.result.ClientDetailResultDTO.DetailOption;
import com.quiz.learning.Demo.repository.AnswerRepository;
import com.quiz.learning.Demo.repository.QuizRepository;
import com.quiz.learning.Demo.repository.ResultRepository;
import com.quiz.learning.Demo.repository.UserRepository;
import com.quiz.learning.Demo.service.specification.ResultSpecs;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

@Service
public class AdminResultService {

    private final ResultRepository resultRepository;
    private final AdminUserService adminUserService;
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final AdminAnswerService adminAnswerService;
    private final ResultSpecs resultSpecs;
    private final AnswerRepository answerRepository;

    public AdminResultService(ResultRepository resultRepository, AdminUserService adminUserService,
            QuizRepository quizRepository, AdminAnswerService adminAnswerService, ResultSpecs resultSpecs,
            AnswerRepository answerRepository, UserRepository userRepository) {
        this.resultRepository = resultRepository;
        this.quizRepository = quizRepository;
        this.adminUserService = adminUserService;
        this.adminAnswerService = adminAnswerService;
        this.resultSpecs = resultSpecs;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
    }

    public Result handleGetResult(Long id) {
        Optional<Result> checkResult = this.resultRepository.findById(id);
        if (checkResult.isEmpty()) {
            throw new ObjectNotFound("Result with id: " + id + " is not existed");
        }
        return checkResult.get();
    }

    public FetchAdminDTO.FetchResultDTO convertToDTO(Result result) {
        FetchAdminDTO.FetchResultDTO dto = new FetchAdminDTO.FetchResultDTO();
        dto.setId(result.getId());
        dto.setScore(result.getScore());
        dto.setSubmittedAt(result.getSubmittedAt());
        dto.setUserId(result.getUser() == null ? null : result.getUser().getId());
        dto.setQuizId(result.getQuiz() == null ? null : result.getQuiz().getId());
        dto.setAnswers(result.getAnswers() == null ? Collections.emptyList()
                : result.getAnswers().stream().map(adminAnswerService::convertToDTO).collect(Collectors.toList()));
        dto.setDuration(result.getDuration());
        Optional<Quiz> checkQuiz = this.quizRepository.findById(dto.getQuizId());
        if (checkQuiz.isEmpty()) {
            throw new ObjectNotFound("Quiz with id: " + dto.getQuizId() + " is not existed");
        }
        dto.setQuizTilte(checkQuiz.get().getTitle());
        Optional<User> checkUser = this.userRepository.findById(dto.getUserId());
        if (checkUser.isEmpty()) {
            throw new ObjectNotFound("User with id: " + dto.getUserId() + " is not existed");
        }
        dto.setUserName(checkUser.get().getFullName());
        dto.setTotalCorrectedAnswer(result.getTotalCorrectedAnswer());
        dto.setTotalQuestions(result.getTotalQuestions());

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

    public FetchResultDTO handleGetResultById(Long id) {
        return this.convertToDTO(this.handleGetResult(id));
    }

    public FetchResultPaginationDTO handleFetchAllResults(int page, int size, String sortBy, String order,
            ResultFilter filter) {

        FetchResultPaginationDTO dto = new FetchResultPaginationDTO();
        Pageable pageable = this.handlePagination(page, size, sortBy, order);
        Specification<Result> spec = this.resultSpecs.build(filter);

        Page<Result> pageResults = this.resultRepository.findAll(spec, pageable);
        List<Result> results = pageResults.getContent();
        Metadata metadata = new Metadata();
        metadata.setCurrentPage(page);
        metadata.setPageSize(size);
        metadata.setTotalObjects(pageResults.getTotalElements());
        metadata.setTotalPages(pageResults.getTotalPages());
        metadata.setHasNext(pageResults.hasNext());
        metadata.setHasPrevious(pageResults.hasPrevious());

        dto.setMetadata(metadata);

        dto.setResults(results.stream().map(this::convertToDTO).toList());

        return dto;
    }

    public FetchResultPaginationDTO handleFetchResultsByQuizId(Long id, int page, int size, String sortBy,
            String order,
            ResultFilter filter) {
        Pageable pageable = this.handlePagination(page, size, sortBy, order);
        Specification<Result> spec = resultSpecs.build(filter);
        filter.setQuizId(id);
        Page<Result> pageResults = this.resultRepository.findAll(spec, pageable);
        List<Result> results = pageResults.getContent();

        FetchResultPaginationDTO dto = new FetchResultPaginationDTO();
        dto.setResults(results
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));

        Metadata metadata = new Metadata();
        metadata.setCurrentPage(page);
        metadata.setPageSize(size);
        metadata.setTotalObjects(pageResults.getTotalElements());
        metadata.setTotalPages(pageResults.getTotalPages());
        metadata.setHasNext(pageResults.hasNext());
        metadata.setHasPrevious(pageResults.hasPrevious());

        dto.setMetadata(metadata);

        return dto;
    }

    public FetchResultPaginationDTO handleFetchResultsByUserId(Long userId, int page, int size, String sortBy,
            String order,
            ResultFilter filter) {
        Pageable pageable = this.handlePagination(page, size, sortBy, order);
        Specification<Result> spec = resultSpecs.build(filter);
        filter.setUserId(userId);
        Page<Result> pageResults = this.resultRepository.findAll(spec, pageable);
        List<Result> results = pageResults.getContent();

        FetchResultPaginationDTO dto = new FetchResultPaginationDTO();
        dto.setResults(results
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));

        Metadata metadata = new Metadata();
        metadata.setCurrentPage(page);
        metadata.setPageSize(size);
        metadata.setTotalObjects(pageResults.getTotalElements());
        metadata.setTotalPages(pageResults.getTotalPages());
        metadata.setHasNext(pageResults.hasNext());
        metadata.setHasPrevious(pageResults.hasPrevious());

        dto.setMetadata(metadata);

        return dto;
    }

    public String handleFetchAverageDuration() {
        List<Result> results = this.resultRepository.findAll();
        if (results == null || results.isEmpty()) {
            return "Không có dữ liệu";
        }
        long totalSeconds = 0;
        long count = 0;
        for (Result res : results) {
            if (res.getDuration() != null) {
                totalSeconds += res.getDuration();
                count++;
            }
        }
        if (count == 0)
            return "Không có dữ liệu phù hợp";
        long avgSeconds = totalSeconds / count;
        long minutes = avgSeconds / 60;
        long seconds = avgSeconds % 60;

        return minutes + " phút " + seconds + " giây";
    }

    public void handleDeleteResult(Result result) {
        this.resultRepository.delete(result);
    }

    public void handleDeleteResultById(Long id) {
        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFound("Result not found"));

        // Xóa tất cả Answer của result đó
        answerRepository.deleteAllByResultId(id);

        // Sau đó xóa result
        resultRepository.delete(result);
    }

}
