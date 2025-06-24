package com.quiz.learning.Demo.domain.restResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestResponse<T> {
    private int statusCode;
    private String error;
    private Object message; // Do message trả ra có thể là String hoặc ArrayList
    private T data;

}
