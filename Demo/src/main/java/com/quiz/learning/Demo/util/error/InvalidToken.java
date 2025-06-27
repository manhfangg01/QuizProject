package com.quiz.learning.Demo.util.error;

public class InvalidToken extends RuntimeException {
    public InvalidToken(String message) {
        super(message);
    }

}
