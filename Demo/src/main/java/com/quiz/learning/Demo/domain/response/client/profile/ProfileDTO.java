package com.quiz.learning.Demo.domain.response.client.profile;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDTO {
    private String email;
    private String avatar;
    private String username;
    private String about;
    private Instant createdAt;

}
