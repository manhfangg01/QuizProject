package com.quiz.learning.Demo.domain.request.client.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientRequestUserUpdate {
    private Long id;
    private String fullName;
    private String about;
}
