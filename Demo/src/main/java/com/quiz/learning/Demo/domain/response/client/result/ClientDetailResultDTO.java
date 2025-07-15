package com.quiz.learning.Demo.domain.response.client.result;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDetailResultDTO {
    private Long resultId;
    private String quizTitle;
    private int totalQuestions;
    private int totalCorrectedAnswers;
    private int totalWrongAnswers;
    private int totalSkippedAnswers;
    private int score;
    private Long duration;
    private Instant submittedAt;
    private Double accuracy;
    private List<DetailAnswer> answers;

    @Getter
    @Setter
    public static class DetailAnswer {
        private Long questionId;
        private String questionContext;
        private List<DetailOption> options;
        private Long selectedOptionId;
        private Boolean isCorrect;

    }

    @Getter
    @Setter
    public static class DetailOption {
        private Long optionId;
        private String optionContext;
        private Boolean isCorrect;
    }

}
