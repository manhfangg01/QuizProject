package com.quiz.learning.Demo.service.admin;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    private final OptionRepository optionRepository;

    public AdminQuestionService(QuestionRepository questionRepository, AdminOptionService adminOptionService,
            QuizRepository quizRepository, OptionRepository optionRepository) {
        this.questionRepository = questionRepository;
        this.adminOptionService = adminOptionService;
        this.quizRepository = quizRepository;
        this.optionRepository = optionRepository;
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

    public List<FetchAdminDTO.FetchQuestionDTO> handleFetchAllQuestions() {
        List<Question> questions = this.questionRepository.findAll();
        return questions == null ? Collections.emptyList()
                : questions
                        .stream()
                        .map(ques -> {
                            return this.convertToDTO(ques);
                        }).collect(Collectors.toList());
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
                            return optionRepository.findById(id).isPresent() ? optionRepository.findById(id).get()
                                    : null;
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
        question.setOptions(this.fetchOptionsByIds(newQuestion.getOptions()));
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

        question.setOptions(this.fetchOptionsByIds(updatedQuestion.getOptions()));
        // Lưu và trả về DTO
        Question saved = questionRepository.save(question);
        return convertToDTO(saved);
    }

    public void handleDeleteQuestion(Long id) {
        Optional<Question> checkQuestion = this.questionRepository.findById(id);
        if (checkQuestion.isEmpty()) {
            throw new ObjectNotFound("Question not found");
        }

        Question question = checkQuestion.get();

        for (Quiz quiz : question.getQuizzes()) {
            quiz.getQuestions().remove(question);
            quizRepository.save(quiz); // cập nhật thay đổi vào DB
        }

        questionRepository.delete(question);
    }

}
