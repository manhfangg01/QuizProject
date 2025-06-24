package com.quiz.learning.Demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Question;
import com.quiz.learning.Demo.domain.Quiz;
import com.quiz.learning.Demo.domain.Result;
import com.quiz.learning.Demo.domain.response.DisplayClientDTO;
import com.quiz.learning.Demo.domain.response.FetchClientDTO;
import com.quiz.learning.Demo.repository.QuizRepository;
import com.quiz.learning.Demo.util.error.DuplicatedObjectException;
import com.quiz.learning.Demo.util.error.NullObjectException;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

@Service
public class QuizService {
    private final QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    // Admin Service

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

    public Quiz setProperties(Quiz quiz1, Quiz quiz2) {
        quiz1.setTitle(quiz2.getTitle());
        quiz1.setResults(quiz2.getResults());
        quiz1.setSubjectName(quiz2.getSubjectName());
        quiz1.setTimeLimit(quiz2.getTimeLimit());
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

    // Client Service

    public DisplayClientDTO.QuizPlayDTO handleClientDisplayQuiz(long id) throws ObjectNotFound {
        Optional<Quiz> checkQuiz = this.quizRepository.findById(id);
        if (checkQuiz.isEmpty()) {
            throw new ObjectNotFound("Quiz Not Found");
        }

        Quiz realQuiz = checkQuiz.get();

        DisplayClientDTO.QuizPlayDTO quizPlayDTO = new DisplayClientDTO.QuizPlayDTO();
        quizPlayDTO.setQuizId(realQuiz.getId());
        quizPlayDTO.setTimeLimit(realQuiz.getTimeLimit());
        quizPlayDTO.setTitle(realQuiz.getTitle());

        List<DisplayClientDTO.QuestionDTO> listQuestionPlay = new ArrayList<>();

        if (realQuiz.getQuestions() != null) {
            listQuestionPlay = realQuiz.getQuestions().stream().map(question -> {
                DisplayClientDTO.QuestionDTO questionDTO = new DisplayClientDTO.QuestionDTO();
                questionDTO.setQuestionId(question.getId());
                questionDTO.setContent(question.getContext());

                List<DisplayClientDTO.OptionDTO> optionDTOs = question.getOptions().stream().map(option -> {
                    DisplayClientDTO.OptionDTO optionDTO = new DisplayClientDTO.OptionDTO();
                    optionDTO.setOptionId(option.getId());
                    optionDTO.setContent(option.getContext());
                    return optionDTO;
                }).collect(Collectors.toList());

                questionDTO.setOptions(optionDTOs);
                return questionDTO;
            }).collect(Collectors.toList());
        }

        quizPlayDTO.setQuestions(listQuestionPlay);
        return quizPlayDTO;
    }

    public List<FetchClientDTO.QuizClientDTO> handleClientFetchQuizzies() {
        List<Quiz> allQuizzies = this.quizRepository.findAll();
        if (allQuizzies.size() == 0) {
            return null;
        }
        List<FetchClientDTO.QuizClientDTO> quizzies = allQuizzies.stream().map(quiz -> {
            FetchClientDTO.QuizClientDTO quizziesDTO = new FetchClientDTO.QuizClientDTO();
            quizziesDTO.setId(quiz.getId());
            quizziesDTO.setNumberOfQuestion(quiz.getQuestions() != null ? quiz.getQuestions().size() : 0);
            quizziesDTO.setActive(quiz.isActive());
            quizziesDTO.setDifficulty(quiz.getDifficulty());
            quizziesDTO.setTimeLimit(quiz.getTimeLimit());
            quizziesDTO.setTitle(quiz.getTitle());
            return quizziesDTO; // 👈 BẮT BUỘC PHẢI CÓ RETURN
        }).collect(Collectors.toList());

        return quizzies;

    }

}
