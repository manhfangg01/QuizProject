package com.quiz.learning.Demo.domain.request.client;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestSubmissionDTO {
    private Long quizId;
    private Long userId;
    private Long duration; // tính bằng giây
    private List<SubmittedAnswer> answers; // 1 trong những phần tử mà null thì coi như skipped
    private Map<Long, Map<Long, String>> optionLabelMap;
}
