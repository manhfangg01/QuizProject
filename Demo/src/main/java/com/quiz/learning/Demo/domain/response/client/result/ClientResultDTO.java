package com.quiz.learning.Demo.domain.response.client.result;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientResultDTO {
    private String title;
    private Instant submittedAt;
    private int score;
    private Long duration;

}
