package com.quiz.learning.Demo.domain.restResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestResponse<T> {
    private int statusCode;
    private String error;
    private Object message; // Do message trả ra có thể là String hoặc ArrayList
    private T data;

}
