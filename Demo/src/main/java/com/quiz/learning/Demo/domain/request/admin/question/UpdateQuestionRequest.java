package com.quiz.learning.Demo.domain.request.admin.question;

import java.util.List;

import com.quiz.learning.Demo.domain.request.admin.option.UpdateOptionRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateQuestionRequest {
    private long id;
    private String context;
    private List<UpdateOptionRequest> options;
}
