package com.quiz.learning.Demo.service.admin;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Option;
import com.quiz.learning.Demo.domain.Question;
import com.quiz.learning.Demo.domain.Quiz;
import com.quiz.learning.Demo.domain.request.admin.question.CreateQuestionRequest;
import com.quiz.learning.Demo.domain.request.admin.question.UpdateQuestionRequest;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.repository.OptionRepository;
import com.quiz.learning.Demo.repository.QuestionRepository;
import com.quiz.learning.Demo.repository.QuizRepository;
import com.quiz.learning.Demo.util.error.DuplicatedObjectException;
import com.quiz.learning.Demo.util.error.NullObjectException;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

@Service
public class AdminQuestionService {
    private final QuestionRepository questionRepository;
    private final AdminOptionService adminOptionService;
    private final QuizRepository quizRepository;

    public AdminQuestionService(QuestionRepository questionRepository, AdminOptionService adminOptionService,
            QuizRepository quizRepository) {
        this.questionRepository = questionRepository;
        this.adminOptionService = adminOptionService;
        this.quizRepository = quizRepository;
    }

    public FetchAdminDTO.FetchQuestionDTO convertToDTO(Question question) {
        FetchAdminDTO.FetchQuestionDTO dto = new FetchAdminDTO.FetchQuestionDTO();
        dto.setQuestionId(question.getId());
        dto.setContext(question.getContext());

        if (question.getOptions() == null) {
            question.setOptions(Collections.emptyList());
        } else {
            List<FetchAdminDTO.FetchOptionDTO> optionDTOs = question.getOptions()
                    .stream()
                    .map(adminOptionService::convertToDTO)
                    .collect(Collectors.toList());
            dto.setOptions(optionDTOs);
        }

        return dto;
    }

    public List<FetchAdminDTO.FetchQuestionDTO> handleFetchAllQuestions() {
        return this.questionRepository.findAll()
                .stream()
                .map(ques -> {
                    return this.convertToDTO(ques);
                }).collect(Collectors.toList());
    }

    public FetchAdminDTO.FetchQuestionDTO handleFetchOneQuestion(long id) {
        Optional<Question> checkQuestion = this.questionRepository.findById(id);
        if (checkQuestion.isEmpty()) {
            throw new ObjectNotFound("Question not found");
        }
        return convertToDTO(checkQuestion.get());
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
        List<Option> options = newQuestion.getOptions().stream()
                .map(optReq -> {
                    Option option = new Option();
                    option.setContext(optReq.getContext());
                    option.setCorrect(optReq.isCorrect());
                    option.setQuestion(question); // gán quan hệ ngược
                    return option;
                })
                .collect(Collectors.toList());

        question.setOptions(options);

        // Lưu và trả về DTO
        Question saved = questionRepository.save(question);
        return convertToDTO(saved);
    }

    public FetchAdminDTO.FetchQuestionDTO handleUpdateQuestion(UpdateQuestionRequest updatedQuestion) {
        Optional<Question> checkQuestion = this.questionRepository.findById(updatedQuestion.getId());
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
        // Map từ CreateOptionRequest -> Option
        List<Option> options = updatedQuestion.getOptions().stream()
                .map(optReq -> {
                    Option option = new Option();
                    option.setContext(optReq.getContext());
                    option.setCorrect(optReq.isCorrect());
                    option.setQuestion(question); // gán quan hệ ngược
                    return option;
                })
                .collect(Collectors.toList());

        question.setOptions(options);
        // Lưu và trả về DTO
        Question saved = questionRepository.save(question);
        return convertToDTO(saved);
    }

    public void handleDeleteQuestion(long id) {
        Optional<Question> checkQuestion = this.questionRepository.findById(id);
        if (checkQuestion.isEmpty()) {
            throw new ObjectNotFound("Question not found");
        }

        Question question = checkQuestion.get();

        for (Quiz quiz : question.getQuizzies()) {
            quiz.getQuestions().remove(question);
            quizRepository.save(quiz); // cập nhật thay đổi vào DB
        }

        questionRepository.delete(question);
    }

}
