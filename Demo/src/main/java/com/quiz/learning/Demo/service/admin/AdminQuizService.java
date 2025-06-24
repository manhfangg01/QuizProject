package com.quiz.learning.Demo.service.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Question;
import com.quiz.learning.Demo.domain.Quiz;
import com.quiz.learning.Demo.domain.Result;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchQuestionDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchResultDTO;
import com.quiz.learning.Demo.domain.response.client.DisplayClientDTO;
import com.quiz.learning.Demo.domain.response.client.FetchClientDTO;
import com.quiz.learning.Demo.repository.QuizRepository;
import com.quiz.learning.Demo.util.error.DuplicatedObjectException;
import com.quiz.learning.Demo.util.error.NullObjectException;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

@Service
public class AdminQuizService {
    private final QuizRepository quizRepository;

    public AdminQuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public List<Quiz> handleFetchAllQuizzies() {
        return this.quizRepository.findAll();
    }

    public Quiz handleFetchQuizById(long id) throws ObjectNotFound {
        Optional<Quiz> checkQuiz = this.quizRepository.findById(id);
        if (checkQuiz.isEmpty()) {
            throw new ObjectNotFound("Quiz Not Found");
        }
        return checkQuiz.get();
    }

    public Quiz handleCreateQuiz(Quiz quiz) throws NullObjectException, DuplicatedObjectException {
        if (quiz == null) {
            throw new NullObjectException("Quiz Is Null");
        }
        if (this.quizRepository.findByTitle(quiz.getTitle()).isPresent()) {
            throw new DuplicatedObjectException("Quiz's name is duplicated");
        }
        return this.quizRepository.save(quiz);
    }

    private Quiz setProperties(Quiz quiz1, Quiz quiz2) {
        quiz1.setTitle(quiz2.getTitle());
        quiz1.setSubjectName(quiz2.getSubjectName());
        quiz1.setQuestions(quiz2.getQuestions());
        quiz1.setDifficulty(quiz2.getDifficulty());
        quiz1.setTotalParticipants(quiz2.getTotalParticipants());
        return quiz1;
    }

    public Quiz handleUpdateQuiz(Quiz updatedQuiz) throws ObjectNotFound, DuplicatedObjectException {
        Optional<Quiz> checkQuiz = this.quizRepository.findById(updatedQuiz.getId());
        if (checkQuiz.isEmpty()) {
            throw new ObjectNotFound("Quiz Not Found");
        }
        if (this.quizRepository.findByTitle(updatedQuiz.getTitle()).isPresent()) {
            if (!updatedQuiz.getTitle().equalsIgnoreCase(checkQuiz.get().getTitle())) {
                throw new DuplicatedObjectException("Quiz's name is duplicated");
            }
        }
        return this.quizRepository.save(this.setProperties(checkQuiz.get(), updatedQuiz));
    }

    public void handleDeleteQuiz(long id) throws ObjectNotFound {
        Optional<Quiz> checkQuiz = this.quizRepository.findById(id);
        if (checkQuiz.isEmpty()) {
            throw new ObjectNotFound("Quiz Not Found");
        }
        this.quizRepository.delete(checkQuiz.get());
    }

}
