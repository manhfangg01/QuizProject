package com.quiz.learning.Demo.domain.response.client.quiz;

import java.time.Instant;
import java.util.List;

import com.quiz.learning.Demo.domain.metadata.Metadata;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailedQuiz {
    private String subject;
    private String quizTitle;
    private long timeLimit;
    private long totalQuestions;
    private int totalParts;
    private int totalComments;
    private long totalParticipants;
    private List<RecentResult> results;

    @Getter
    @Setter
    public static class RecentResult {
        private Long resultId;
        private Instant submittedDate;
        private long totalQuestions;
        private long score;
        private long duration;
    }

}
