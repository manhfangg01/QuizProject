package com.quiz.learning.Demo.service.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Answer;
import com.quiz.learning.Demo.domain.Option;
import com.quiz.learning.Demo.domain.Question;
import com.quiz.learning.Demo.domain.request.client.SubmittedAnswer;
import com.quiz.learning.Demo.domain.response.client.result.ClientDetailResultDTO.DetailAnswer;
import com.quiz.learning.Demo.domain.response.client.result.ClientDetailResultDTO.DetailOption;
import com.quiz.learning.Demo.repository.AnswerRepository;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

@Service
public class ClientAnswerService {
    private final AnswerRepository answerRepository;

    private final ClientOptionService clientOptionService;

    public ClientAnswerService(AnswerRepository answerRepository, ClientOptionService clientOptionService) {
        this.answerRepository = answerRepository;
        this.clientOptionService = clientOptionService;
    }

    public SubmittedAnswer convertToAnswer(Answer answer) {
        if (answer == null)
            return null;
        SubmittedAnswer submittedAnswer = new SubmittedAnswer();
        if (answer.getSelectedOption() != null) {
            submittedAnswer.setQuestionId(answer.getSelectedOption().getQuestion() == null ? null
                    : answer.getSelectedOption().getQuestion().getId());
            submittedAnswer.setSelectedOptionId(answer.getSelectedOption().getId());
        }
        return submittedAnswer;
    }

    public DetailAnswer handleFetchDetailAnswer(Long answerId) {
        DetailAnswer dto = new DetailAnswer();
        Optional<Answer> checkAnswer = this.answerRepository.findById(answerId);
        Answer realAnswer = checkAnswer
                .orElseThrow(() -> new ObjectNotFound("Không tồn tại câu trả lời nào có id " + answerId));

        Option selectedOption = realAnswer.getSelectedOption();
        if (selectedOption != null) {
            Question ques = selectedOption.getQuestion();
            List<Option> options = ques.getOptions();
            Option correctedOption = options.stream()
                    .filter(opt -> opt.getIsCorrect() == true)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Không tìm thấy đáp án đúng cho câu hỏi"));

            dto.setExplaination(ques.getExplaination());
            dto.setQuestionContext(ques.getContext());
            dto.setQuestionId(ques.getId());
            dto.setSelectedOptionId(selectedOption.getId());

            // Sửa ở đây: Thay Collections.emptyList() bằng new ArrayList<>()
            List<DetailOption> detailOptions = new ArrayList<>(); // Collections.emptyList() Không nên dùng do list là
                                                                  // immutable
            detailOptions.add(this.clientOptionService.convertToDetailDto(correctedOption));
            detailOptions.add(this.clientOptionService.convertToDetailDto(selectedOption));

            dto.setOptions(detailOptions);
            dto.setCorrectedOptionId(correctedOption.getId());
        }

        dto.setAnswerId(answerId);
        dto.setCorrectedOptionLabel(realAnswer.getCorrectedOptionLabel());
        dto.setSelectedOptionLabel(realAnswer.getSelectedOptionLabel());
        dto.setIsCorrect(realAnswer.getCorrectedOptionLabel().equalsIgnoreCase(realAnswer.getSelectedOptionLabel()));
        return dto;
    }
}
