package com.quiz.learning.Demo.domain.response.client.profile;

import java.time.Instant;
import java.util.List;

import com.quiz.learning.Demo.domain.response.client.result.ClientResultDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDTO {
    private String email;
    private String avatar;
    private String username;
    private String about;
    private List<ClientResultDTO> results;
    private Instant createdAt;

}
