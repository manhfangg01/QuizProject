package com.quiz.learning.Demo.service.client;

import java.time.Instant;
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
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.learning.Demo.domain.Answer;
import com.quiz.learning.Demo.domain.Option;
import com.quiz.learning.Demo.domain.Question;
import com.quiz.learning.Demo.domain.Quiz;
import com.quiz.learning.Demo.domain.QuizProgress;
import com.quiz.learning.Demo.domain.Result;
import com.quiz.learning.Demo.domain.User;
import com.quiz.learning.Demo.domain.filterCriteria.client.QuizClientFilter;
import com.quiz.learning.Demo.domain.metadata.Metadata;
import com.quiz.learning.Demo.domain.request.client.RequestExitingQuiz;
import com.quiz.learning.Demo.domain.request.client.RequestSavingProgress;
import com.quiz.learning.Demo.domain.request.client.RequestSubmissionDTO;
import com.quiz.learning.Demo.domain.request.client.SubmittedAnswer;
import com.quiz.learning.Demo.domain.response.client.ResponseExitingQuiz;
import com.quiz.learning.Demo.domain.response.client.ResponseSavingProgress;
import com.quiz.learning.Demo.domain.response.client.ResponseSubmissionDTO;
import com.quiz.learning.Demo.domain.response.client.FetchClientDTO.QuizClientDTO;
import com.quiz.learning.Demo.domain.response.client.FetchClientDTO.QuizClientPaginationDTO;
import com.quiz.learning.Demo.domain.response.client.FetchClientDTO.QuizClientPlayDTO;
import com.quiz.learning.Demo.domain.response.client.ResponseSubmissionDTO.Detail;
import com.quiz.learning.Demo.repository.AnswerRepository;
import com.quiz.learning.Demo.repository.OptionRepository;
import com.quiz.learning.Demo.repository.QuestionRepository;
import com.quiz.learning.Demo.repository.QuizProgressRepository;
import com.quiz.learning.Demo.repository.QuizRepository;
import com.quiz.learning.Demo.repository.ResultRepository;
import com.quiz.learning.Demo.repository.UserRepository;
import com.quiz.learning.Demo.service.specification.QuizSpecs;
import com.quiz.learning.Demo.util.error.ObjectNotFound;
import com.quiz.learning.Demo.util.security.SecurityUtil;

@Service
public class ClientQuizService {
    private final QuizRepository quizRepository;
    private final QuizSpecs quizSpecs;
    private final ClientQuestionService clientQuestionService;
    private final ResultRepository resultRepository;
    private final AnswerRepository answerRepository;
    private final OptionRepository optionRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final QuizProgressRepository quizProgressRepository;
    private final ObjectMapper objectMapper;

    public ClientQuizService(QuizRepository quizRepository, QuizSpecs quizSpecs,
            ClientQuestionService clientQuestionService, ResultRepository resultRepository,
            AnswerRepository answerRepository, OptionRepository optionRepository,
            QuestionRepository questionRepository, UserRepository userRepository,
            QuizProgressRepository quizProgressRepository, ObjectMapper objectMapper) {
        this.quizRepository = quizRepository;
        this.quizSpecs = quizSpecs;
        this.clientQuestionService = clientQuestionService;
        this.resultRepository = resultRepository;
        this.answerRepository = answerRepository;
        this.optionRepository = optionRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.quizProgressRepository = quizProgressRepository;
        this.objectMapper = objectMapper;
    }

    public QuizClientDTO convertToDto(Quiz quiz) {
        QuizClientDTO dto = new QuizClientDTO();
        dto.setQuizId(quiz.getId());
        dto.setDifficulty(quiz.getDifficulty());
        dto.setTitle(quiz.getTitle());
        dto.setTimeLimit(quiz.getTimeLimit());
        dto.setTotalParticipants(quiz.getTotalParticipants());
        dto.setTotalQuestions(quiz.getQuestions() == null ? 0 : quiz.getQuestions().size());
        dto.setSubject(quiz.getSubjectName());

        if (quiz.getResults() != null && !quiz.getResults().isEmpty()) {
            Optional<Result> latestResult = quiz.getResults().stream()
                    .filter(res -> Boolean.TRUE.equals(res.getIsLastest()))
                    .findFirst();

            latestResult.ifPresent(result -> dto.setResultId(result.getId()));
        }

        return dto;
    }

    public Pageable handlePagination(int page, int size, String sortBy, String order) {
        // Validate input parameters
        page = Math.max(page, 1); // Đảm bảo page >= 1
        size = Math.max(size, 1); // Đảm bảo size >= 1

        Sort sort = order.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(page - 1, size, sort);
    }

    public QuizClientPaginationDTO handleFetchQuizzes(int page, int size, String sortBy, String order,
            QuizClientFilter filterCriteria) {
        Pageable pageable = this.handlePagination(page, size, sortBy, order);

        Specification<Quiz> spec = (root, query, cb) -> cb.conjunction();
        Specification<Quiz> spec1 = this.quizSpecs.titleContains(filterCriteria.getTitle());
        Specification<Quiz> spec2 = this.quizSpecs.hasDifficulty(filterCriteria.getDifficulty());
        Specification<Quiz> spec3 = this.quizSpecs.timeLimitLessThanOrEqual(filterCriteria.getTimeLimit());
        Specification<Quiz> spec4 = this.quizSpecs.hasSubject(filterCriteria.getSubject());

        spec = spec.and(spec1).and(spec2).and(spec3).and(spec4);

        Page<Quiz> pageQuizzes = this.quizRepository.findAll(spec, pageable);
        List<Quiz> quizzes = pageQuizzes.getContent();

        // Gán DTO
        QuizClientPaginationDTO dto = new QuizClientPaginationDTO();
        Metadata metadata = new Metadata();
        metadata.setCurrentPage(page);
        metadata.setPageSize(size);
        metadata.setTotalObjects(pageQuizzes.getTotalElements());
        metadata.setTotalPages(pageQuizzes.getTotalPages()); // Sửa: bỏ -1
        metadata.setHasNext(pageQuizzes.hasNext()); // Sử dụng method có sẵn
        metadata.setHasPrevious(pageQuizzes.hasPrevious()); // Sử dụng method có sẵn

        dto.setMetadata(metadata);
        dto.setQuizzes(quizzes.stream().map(this::convertToDto).collect(Collectors.toList()));
        return dto;

    }

    public QuizClientPlayDTO handleDisplayQuiz(Long id) {
        Optional<Quiz> checkQuiz = this.quizRepository.findById(id);
        if (checkQuiz.isEmpty()) {
            throw new ObjectNotFound("Tập câu hỏi không tồn tại");
        }
        Quiz realQuiz = checkQuiz.get();
        QuizClientPlayDTO dto = new QuizClientPlayDTO();
        dto.setQuizId(realQuiz.getId());
        dto.setDifficulty(realQuiz.getDifficulty());
        dto.setIsActive(realQuiz.getIsActive());
        dto.setNumberOfQuestion(realQuiz.getQuestions() == null ? 0 : realQuiz.getQuestions().size());
        dto.setSubjectName(realQuiz.getSubjectName());
        dto.setTimeLimit(realQuiz.getTimeLimit());
        dto.setTitle(realQuiz.getTitle());
        dto.setTotalParticipants(realQuiz.getTotalParticipants());
        dto.setQuestions(realQuiz.getQuestions() == null ? Collections.emptyList()
                : realQuiz.getQuestions().stream().map(clientQuestionService::convertToDto).toList());
        return dto;

    }

    public ResponseSubmissionDTO handleSubmitAnswer(RequestSubmissionDTO submissionDTO) {
        Optional<Quiz> checkQuiz = quizRepository.findById(submissionDTO.getQuizId());
        if (checkQuiz.isEmpty()) {
            throw new ObjectNotFound("Tập câu hỏi không tồn tại");
        }

        Quiz realQuiz = checkQuiz.get();
        ResponseSubmissionDTO res = new ResponseSubmissionDTO();
        Instant now = Instant.now();
        int totalQuestions = realQuiz.getQuestions() == null ? 0 : realQuiz.getQuestions().size();

        // Tạo kết quả ban đầu
        Result result = new Result();
        result.setQuiz(realQuiz);
        result.setUser(userRepository.findById(submissionDTO.getUserId()).orElse(null));
        result.setSubmittedAt(now);
        result.setDuration(submissionDTO.getDuration());
        result.setTotalQuestions(totalQuestions);
        result.setTotalCorrectedAnswer(0); // tạm thời
        result.setTotalWrongAnswer(0);
        result.setScore(0); // tạm thời

        result = resultRepository.save(result); // lưu để có ID

        int correctCount = 0;
        List<Detail> detailList = new ArrayList<>();
        List<Answer> answerList = new ArrayList<>();

        if (submissionDTO.getAnswers() != null) {
            result.setTotalSkippedAnswer(realQuiz.getQuestions() == null ? 0
                    : realQuiz.getQuestions().size() - submissionDTO.getAnswers().size());
            for (SubmittedAnswer submittedAnswer : submissionDTO.getAnswers()) {
                // Lấy selected Option
                Optional<Option> selectedOptionOpt = optionRepository.findById(submittedAnswer.getSelectedOptionId());
                if (selectedOptionOpt.isEmpty()) {
                    throw new ObjectNotFound("Lựa chọn không tồn tại: ID = " + submittedAnswer.getSelectedOptionId());
                }

                Option selectedOption = selectedOptionOpt.get();
                Question question = selectedOption.getQuestion(); // lấy question từ option

                // Lấy đáp án đúng
                Optional<Option> correctOptionOpt = question.getOptions()
                        .stream()
                        .filter(Option::getIsCorrect)
                        .findFirst();

                Long correctOptionId = correctOptionOpt.map(Option::getId).orElse(null);
                boolean isCorrect = correctOptionId != null && correctOptionId.equals(selectedOption.getId());

                if (isCorrect)
                    correctCount++;

                // Tạo Answer
                Answer answer = new Answer();
                answer.setIsCorrect(isCorrect);
                answer.setSelectedOption(selectedOption);
                answer.setResult(result);
                answerList.add(answer);

                // Tạo Detail để trả về
                Detail detail = new Detail();
                detail.setQuestionId(question.getId());
                detail.setCorrectOptionId(correctOptionId);
                detail.setSelectedOptionId(selectedOption.getId());
                detail.setIsCorrect(isCorrect);
                detailList.add(detail);
            }

            // Lưu danh sách Answer
            answerRepository.saveAll(answerList);
        }

        // Cập nhật lại result với số câu đúng và điểm
        result.setTotalCorrectedAnswer(correctCount);
        result.setTotalWrongAnswer(totalQuestions - result.getTotalCorrectedAnswer() - result.getTotalSkippedAnswer());
        result.setScore(correctCount);
        if (realQuiz.getResults() != null) {
            for (Result existedResult : realQuiz.getResults()) {
                existedResult.setIsLastest(false);
            }
        }
        result.setIsLastest(true);
        resultRepository.save(result);

        // Trả kết quả về cho client
        res.setQuizId(realQuiz.getId());
        res.setUserId(submissionDTO.getUserId());
        res.setSubmittedAt(now);
        res.setDuration(submissionDTO.getDuration());
        res.setTotalQuestions(totalQuestions);
        res.setTotalCorrectedAnswer(correctCount);
        res.setScore(correctCount * 10);
        res.setDetails(detailList);
        res.setResultId(result.getId());

        // Xóa đi những progress tạm thời liên quan
        this.quizProgressRepository.deleteAllByUserIdAndQuizId(submissionDTO.getUserId(), submissionDTO.getQuizId());

        return res;
    }

    public ResponseSavingProgress handleSaveProgress(RequestSavingProgress request) {
        Quiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() -> new ObjectNotFound("Quiz not found"));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ObjectNotFound("User not found"));

        // 2. Lưu hoặc cập nhật tiến độ (theo entity QuizProgress)
        Optional<QuizProgress> existingProgress = quizProgressRepository
                .findByUserAndQuiz(user, quiz);

        QuizProgress progress = existingProgress.orElse(new QuizProgress());
        progress.setQuiz(quiz);
        progress.setUser(user);
        progress.setDuration(request.getDuration());
        progress.setSavedAt(Instant.now());

        // Giả sử bạn lưu danh sách answer dưới dạng JSON
        String answersJson;
        try {
            answersJson = objectMapper.writeValueAsString(request.getAnswers());
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize answers", e);
        }
        progress.setAnswersJson(answersJson);

        quizProgressRepository.save(progress);

        // 3. Trả về kết quả
        ResponseSavingProgress dto = new ResponseSavingProgress();
        dto.setQuizId(quiz.getId());
        dto.setUserId(user.getId());
        dto.setSavedAt(progress.getSavedAt());
        return dto;
    }

    public ResponseExitingQuiz handleExitQuiz(RequestExitingQuiz request) {
        // "Do cùng là lưu progress chỉ khác là save được gọi tự động, còn exit chỉ được
        // gọi khi người dùng chủ động thoát" => Map DTO qua sau đó sử dụng logic của
        // saving
        RequestSavingProgress saveRequest = new RequestSavingProgress();
        saveRequest.setQuizId(request.getQuizId());
        saveRequest.setUserId(request.getUserId());
        saveRequest.setDuration(request.getDuration());
        saveRequest.setAnswers(request.getAnswers());

        ResponseSavingProgress saved = handleSaveProgress(saveRequest);

        ResponseExitingQuiz response = new ResponseExitingQuiz();
        response.setQuizId(saved.getQuizId());
        response.setUserId(saved.getUserId());
        response.setSavedAt(saved.getSavedAt());
        return response;
    }

}
