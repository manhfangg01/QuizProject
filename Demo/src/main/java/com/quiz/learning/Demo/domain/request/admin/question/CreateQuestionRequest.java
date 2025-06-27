package com.quiz.learning.Demo.domain.request.admin.question;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.quiz.learning.Demo.domain.request.admin.option.CreateOptionRequest;

@Getter
@Setter
public class CreateQuestionRequest {
    private String context;
    private List<Long> options;
}
