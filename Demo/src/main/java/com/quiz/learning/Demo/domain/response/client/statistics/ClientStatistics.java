package com.quiz.learning.Demo.domain.response.client.statistics;

import java.time.Instant;
import java.util.List;

import com.quiz.learning.Demo.domain.metadata.Metadata;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientStatistics {
    private long totalDoneQuizzes;
    private long totalSpentTime;
    private String goal;
    private double accuracy;
    private long averageTime;
    private double averageMark;
    private double highestMark;
    private Metadata metadata;
    private List<DoneResult> results;

    @Getter
    @Setter
    public static class DoneResult {
        private Instant submittedDate;
        private String quizTitle;
        private String subject;
        private long finalMark;
        private long spentTime;
    }

}
