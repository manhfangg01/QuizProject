package com.quiz.learning.Demo.service.admin.relationServices;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Question;
import com.quiz.learning.Demo.domain.Quiz;
import com.quiz.learning.Demo.repository.QuestionRepository;
import com.quiz.learning.Demo.repository.QuizRepository;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

@Service
public class AdminQuestionRelationQuiz {
    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;

    public AdminQuestionRelationQuiz(QuestionRepository questionRepository, QuizRepository quizRepository) {
        this.questionRepository = questionRepository;
        this.quizRepository = quizRepository;
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
