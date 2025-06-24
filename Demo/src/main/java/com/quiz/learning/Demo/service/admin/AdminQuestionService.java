package com.quiz.learning.Demo.service.admin;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Question;
import com.quiz.learning.Demo.domain.Quiz;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.repository.QuestionRepository;
import com.quiz.learning.Demo.util.error.DuplicatedObjectException;
import com.quiz.learning.Demo.util.error.NullObjectException;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

@Service
public class AdminQuestionService {
    private final QuestionRepository questionRepository;
    private final AdminOptionService adminOptionService;

    public AdminQuestionService(QuestionRepository questionRepository, AdminOptionService adminOptionService) {
        this.questionRepository = questionRepository;
        this.adminOptionService = adminOptionService;
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

    public List<Question> handleFetchAllQuestions() {
        return this.questionRepository.findAll();
    }

    public Question handleFetchOneQuestion(long id) throws ObjectNotFound {
        Optional<Question> checkQuestion = this.questionRepository.findById(id);
        if (checkQuestion.isEmpty()) {
            throw new ObjectNotFound("Question not found");
        }
        return checkQuestion.get();
    }

    public Question handleCreateQuestion(Question newQuestion) throws NullObjectException, DuplicatedObjectException {
        if (newQuestion == null) {
            throw new NullObjectException("New Question is Null");
        }
        if (this.questionRepository.findByContext(newQuestion.getContext()).isPresent()) {
            throw new DuplicatedObjectException("Question with the similar context is found");
        }
        return this.questionRepository.save(newQuestion);
    }

    private Question setProperties(Question ques1, Question ques2) {
        ques1.setContext(ques2.getContext());
        ques1.setOptions(ques2.getOptions());
        ques1.setQuizzies(ques2.getQuizzies());
        return ques1;
    }

    public Question handleUpdateQuestion(Question updatedQuestion) throws ObjectNotFound, DuplicatedObjectException {
        Optional<Question> checkQuestion = this.questionRepository.findById(updatedQuestion.getId());
        if (checkQuestion.isEmpty()) {
            throw new ObjectNotFound("Question not found");
        }
        Question realQuestion = checkQuestion.get();
        if (this.questionRepository.findByContext(updatedQuestion.getContext()).isPresent()) {
            if (updatedQuestion.getContext() != realQuestion.getContext())
                throw new DuplicatedObjectException("Question with the similar context is found");
        }
        return this.questionRepository.save(setProperties(realQuestion, updatedQuestion));
    }

    public void handleDeleteQuestion(long id) throws ObjectNotFound {
        Optional<Question> checkQuestion = this.questionRepository.findById(id);
        if (checkQuestion.isEmpty()) {
            throw new ObjectNotFound("Question not found");
        }
        this.questionRepository.deleteById(id);
    }

}
