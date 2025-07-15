package com.quiz.learning.Demo.service.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Answer;
import com.quiz.learning.Demo.domain.Option;
import com.quiz.learning.Demo.domain.Question;
import com.quiz.learning.Demo.domain.Quiz;
import com.quiz.learning.Demo.domain.Result;
import com.quiz.learning.Demo.domain.User;
import com.quiz.learning.Demo.domain.metadata.Metadata;
import com.quiz.learning.Demo.domain.response.client.AnswerReviewDTO;
import com.quiz.learning.Demo.domain.response.client.ClientPaginationResultHistory;
import com.quiz.learning.Demo.domain.response.client.ClientResultHistoryDTO;
import com.quiz.learning.Demo.domain.response.client.ResponseClientQuizResultDTO;
import com.quiz.learning.Demo.domain.response.client.result.ClientDetailResultDTO;
import com.quiz.learning.Demo.domain.response.client.result.ClientDetailResultDTO.DetailAnswer;
import com.quiz.learning.Demo.domain.response.client.result.ClientDetailResultDTO.DetailOption;
import com.quiz.learning.Demo.repository.QuizRepository;
import com.quiz.learning.Demo.repository.ResultRepository;
import com.quiz.learning.Demo.repository.UserRepository;
import com.quiz.learning.Demo.util.error.ObjectNotFound;
import com.quiz.learning.Demo.util.error.UnauthorizedException;
import com.quiz.learning.Demo.util.security.SecurityUtil;

@Service
public class ClientResultService {
        private final ResultRepository resultRepository;
        private final QuizRepository quizRepository;
        private final UserRepository userRepository;

        public ClientResultService(ResultRepository resultRepository, QuizRepository quizRepository,
                        UserRepository userRepository) {
                this.resultRepository = resultRepository;
                this.quizRepository = quizRepository;
                this.userRepository = userRepository;
        }

        public Pageable handlePagination(int page, int size, String sortBy, String order) {
                // Validate input parameters
                page = Math.max(page, 1); // Đảm bảo page >= 1
                size = Math.max(size, 1); // Đảm bảo size >= 1

                Sort sort = order.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
                return PageRequest.of(page - 1, size, sort);
        }

        public ResponseClientQuizResultDTO getClientQuizResult(Long quizId) {
                String currentUsername = SecurityUtil.getCurrentUserLogin()
                                .orElseThrow(() -> new UnauthorizedException("Người dùng chưa đăng nhập"));

                User user = userRepository.findByEmail(currentUsername)
                                .orElseThrow(() -> new ObjectNotFound("Không tìm thấy người dùng"));

                Quiz quiz = quizRepository.findById(quizId)
                                .orElseThrow(() -> new ObjectNotFound("Quiz không tồn tại"));

                Result result = resultRepository.findByUserAndQuiz(user, quiz)
                                .orElseThrow(() -> new ObjectNotFound("Bạn chưa làm bài quiz này"));

                ResponseClientQuizResultDTO dto = new ResponseClientQuizResultDTO();
                dto.setDuration(result.getDuration());
                dto.setQuizId(quiz.getId());
                dto.setQuizTitle(quiz.getTitle());
                dto.setScore(result.getScore());
                dto.setSubmittedAt(result.getSubmittedAt());
                dto.setTotalCorrect(result.getTotalCorrectedAnswer());
                dto.setTotalQuestions(result.getTotalQuestions());
                List<AnswerReviewDTO> answerDTOs = result.getAnswers().stream().map(answer -> {
                        Option selected = answer.getSelectedOption();
                        Question question = selected.getQuestion();

                        // Tìm option đúng cho câu hỏi
                        Option correctOption = question.getOptions()
                                        .stream()
                                        .filter(Option::getIsCorrect)
                                        .findFirst()
                                        .orElse(null);

                        AnswerReviewDTO a = new AnswerReviewDTO();
                        a.setQuestionId(question.getId());
                        a.setQuestionContent(question.getContext());
                        a.setSelectedOptionId(selected.getId());
                        a.setSelectedOptionContent(selected.getContext());
                        a.setIsCorrect(answer.getIsCorrect());
                        a.setCorrectOptionId(correctOption != null ? correctOption.getId() : null);

                        return a;
                }).collect(Collectors.toList());
                dto.setAnswers(answerDTOs);

                return dto;

        }

        public ClientPaginationResultHistory handleFetchHistory(int page, int size, String sortBy, String order,
                        Long userId) {
                ClientPaginationResultHistory dto = new ClientPaginationResultHistory();
                Pageable pageable = this.handlePagination(page, size, sortBy, order);
                Optional<User> checkUser = this.userRepository.findById(userId);
                if (checkUser.isEmpty()) {
                        throw new ObjectNotFound("User not found");
                }
                User realUser = checkUser.get();
                Page<Result> pageResults = this.resultRepository.findAllByUser(realUser, pageable);
                List<Result> results = pageResults.getContent();
                List<ClientResultHistoryDTO> histories = results.stream().map(res -> {
                        ClientResultHistoryDTO history = new ClientResultHistoryDTO();
                        history.setDuration(res.getDuration());
                        history.setQuizId(res.getQuiz() == null ? null : res.getQuiz().getId());
                        history.setQuizTitle(res.getQuiz() == null ? null : res.getQuiz().getTitle());
                        history.setScore(res.getScore());
                        history.setSubmittedAt(res.getSubmittedAt());
                        history.setTotalCorrect(res.getTotalCorrectedAnswer());
                        history.setTotalQuestions(res.getTotalQuestions());
                        history.setResultId(res.getId());
                        return history;
                }).collect(Collectors.toList());

                Metadata metadata = new Metadata();
                metadata.setCurrentPage(page);
                metadata.setPageSize(size);
                metadata.setTotalObjects(pageResults.getTotalElements());
                metadata.setTotalPages(pageResults.getTotalPages());
                metadata.setHasNext(pageResults.hasNext());
                metadata.setHasPrevious(pageResults.hasPrevious());

                dto.setMetadata(metadata);
                dto.setHistories(histories);
                return dto;

        }

        public ClientDetailResultDTO handleFetchDetailResult(Long resultId) {
                ClientDetailResultDTO dto = new ClientDetailResultDTO();
                Result result = resultRepository.findById(resultId)
                                .orElseThrow(() -> new ObjectNotFound("Result not found"));
                if (result.getQuiz() == null) {
                        throw new ObjectNotFound("Quiz not found");
                }
                Quiz quiz = result.getQuiz();

                if (result.getAnswers() == null) {
                        throw new ObjectNotFound("Answers not found");
                }
                List<Answer> answers = result.getAnswers();

                dto.setResultId(resultId);
                dto.setQuizTitle(quiz.getTitle());
                dto.setTotalQuestions(result.getTotalQuestions());
                dto.setTotalCorrectedAnswers(result.getTotalCorrectedAnswer());
                dto.setTotalWrongAnswers(result.getTotalQuestions() - result.getTotalCorrectedAnswer());
                dto.setTotalSkippedAnswers(
                                (int) answers.stream().filter(ans -> ans.getSelectedOption() == null).count());
                dto.setScore(result.getScore());
                dto.setDuration(result.getDuration());
                dto.setSubmittedAt(result.getSubmittedAt());
                dto.setAccuracy((double) result.getTotalCorrectedAnswer() / result.getTotalQuestions() * 100);

                List<DetailAnswer> answerDTOs = new ArrayList<>();

                for (Answer answer : answers) {
                        Question q = answer.getSelectedOption() != null
                                        ? answer.getSelectedOption().getQuestion()
                                        : answer.getResult().getQuiz().getQuestions().stream()
                                                        .filter(ques -> ques.getOptions().stream()
                                                                        .anyMatch(opt -> opt.getAnswers()
                                                                                        .contains(answer)))
                                                        .findFirst()
                                                        .orElse(null);

                        if (q == null)
                                continue;

                        DetailAnswer answerDTO = new DetailAnswer();
                        answerDTO.setQuestionId(q.getId());
                        answerDTO.setQuestionContext(q.getContext());

                        // Convert options
                        List<DetailOption> optionDTOs = q.getOptions().stream()
                                        .map(opt -> {
                                                DetailOption optionDTO = new DetailOption();
                                                optionDTO.setOptionId(opt.getId());
                                                optionDTO.setOptionContext(opt.getContext());
                                                optionDTO.setIsCorrect(opt.getIsCorrect());
                                                return optionDTO;
                                        }).collect(Collectors.toList());
                        answerDTO.setOptions(optionDTOs);

                        // selected option
                        if (answer.getSelectedOption() != null) {
                                answerDTO.setSelectedOptionId(answer.getSelectedOption().getId());
                                answerDTO.setIsCorrect(answer.getSelectedOption().getIsCorrect());
                        } else {
                                answerDTO.setSelectedOptionId(null);
                                answerDTO.setIsCorrect(false);
                        }

                        answerDTOs.add(answerDTO);
                }

                dto.setAnswers(answerDTOs);

                return dto;
        }

}
