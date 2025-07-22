package com.quiz.learning.Demo.util.error;

public class EnvironmentValueError extends RuntimeException {
    public EnvironmentValueError(String message) {
        super(message);
    }
}
