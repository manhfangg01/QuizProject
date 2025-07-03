package com.quiz.learning.Demo.service.admin;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Question;
import com.quiz.learning.Demo.domain.Quiz;
import com.quiz.learning.Demo.domain.request.admin.quiz.CreateQuizRequest;
import com.quiz.learning.Demo.domain.request.admin.quiz.UpdateQuizRequest;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.repository.QuizRepository;
import com.quiz.learning.Demo.util.error.DuplicatedObjectException;
import com.quiz.learning.Demo.util.error.NullObjectException;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

@Service
public class AdminQuizService {
    private final QuizRepository quizRepository;
    private final AdminQuestionService adminQuestionService;
    private final AdminResultService adminResultService;

    public AdminQuizService(QuizRepository quizRepository, AdminQuestionService adminQuestionService,
            AdminResultService adminResultService) {
        this.quizRepository = quizRepository;
        this.adminQuestionService = adminQuestionService;
        this.adminResultService = adminResultService;

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

    public FetchAdminDTO.FetchQuizDTO convertToDTO(Quiz quiz) {
        FetchAdminDTO.FetchQuizDTO dto = new FetchAdminDTO.FetchQuizDTO();
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

    public List<FetchAdminDTO.FetchQuizDTO> handleFetchAllquizzes() {
        List<Quiz> quizzes = this.quizRepository.findAll();
        List<FetchAdminDTO.FetchQuizDTO> quizDTOs = quizzes == null ? Collections.emptyList()
                : quizzes
                        .stream()
                        .map(quiz -> {
                            return this.convertToDTO(quiz);
                        })
                        .collect(Collectors.toList());

        return quizDTOs;
    }

    public FetchAdminDTO.FetchQuizDTO handleFetchQuizById(Long id) {
        Optional<Quiz> checkQuiz = this.quizRepository.findById(id);
        if (checkQuiz.isEmpty()) {
            throw new ObjectNotFound("Quiz Not Found");
        }
        return convertToDTO(checkQuiz.get());
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

    public FetchAdminDTO.FetchQuizDTO handleCreateQuiz(CreateQuizRequest createdQuiz) {
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
        return convertToDTO(saved);
    }

    public FetchAdminDTO.FetchQuizDTO handleUpdateQuiz(UpdateQuizRequest request) {
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

        return convertToDTO(quizRepository.save(quiz));
    }

    public void handleDeleteQuiz(Long id) {
        Optional<Quiz> checkQuiz = this.quizRepository.findById(id);
        if (checkQuiz.isEmpty()) {
            throw new ObjectNotFound("Quiz Not Found");
        }
        this.quizRepository.delete(checkQuiz.get());
    }

}
