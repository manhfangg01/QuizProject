package com.quiz.learning.Demo.domain.response.client.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientResponseUserUpdate {
    private Long id;
    private String fullName;
    private String about;
    private String avatar;
}
