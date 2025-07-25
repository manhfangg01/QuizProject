package com.quiz.learning.Demo.service.admin.relationServices;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Question;
import com.quiz.learning.Demo.domain.Quiz;
import com.quiz.learning.Demo.repository.QuestionRepository;
import com.quiz.learning.Demo.repository.QuizRepository;
import com.quiz.learning.Demo.service.azure.AzureBlobService;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

@Service
public class AdminQuestionRelationQuiz {
    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;
    private final AzureBlobService azureBlobService;

    public AdminQuestionRelationQuiz(QuestionRepository questionRepository, QuizRepository quizRepository,
            AzureBlobService azureBlobService) {
        this.questionRepository = questionRepository;
        this.quizRepository = quizRepository;
        this.azureBlobService = azureBlobService;
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

        if (question.getQuestionImage() != null && !question.getQuestionImage().isEmpty()) {
            String blobName = azureBlobService.getBlobNameFromUrl(question.getQuestionImage());
            if (blobName != null) {
                azureBlobService.deleteFile(blobName);
            }
        }

        questionRepository.delete(question);
    }

}
