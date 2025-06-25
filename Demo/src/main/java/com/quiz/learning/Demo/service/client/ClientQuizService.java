package com.quiz.learning.Demo.service.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Quiz;
import com.quiz.learning.Demo.domain.response.client.DisplayClientDTO;
import com.quiz.learning.Demo.domain.response.client.FetchClientDTO;
import com.quiz.learning.Demo.repository.QuizRepository;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

@Service
public class ClientQuizService {
    private final QuizRepository quizRepository;

    public ClientQuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public FetchClientDTO.QuizClientDTO convertToDTO(Quiz quiz) {
        FetchClientDTO.QuizClientDTO quizDTO = new FetchClientDTO.QuizClientDTO();
        quizDTO.setId(quiz.getId());
        quizDTO.setNumberOfQuestion(quiz.getQuestions() != null ? quiz.getQuestions().size() : 0);
        quizDTO.setActive(quiz.isActive());
        quizDTO.setDifficulty(quiz.getDifficulty());
        quizDTO.setTimeLimit(quiz.getTimeLimit());
        quizDTO.setTitle(quiz.getTitle());
        return quizDTO;
    }

    public FetchClientDTO.QuizClientDTO handleFetchQuizById(long id) {
        Optional<Quiz> checkQuiz = this.quizRepository.findById(id);
        if (checkQuiz.isEmpty()) {
            throw new ObjectNotFound("Quiz Not Found");
        }
        return this.convertToDTO(checkQuiz.get());
    }

    public DisplayClientDTO.QuizPlayDTO handleClientDisplayQuiz(long id) {
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
        if (allQuizzies.isEmpty()) {
            return null;
        }
        return allQuizzies.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

}
