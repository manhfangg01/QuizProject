package com.quiz.learning.Demo.util.error;

public class WrongCheckPassword extends RuntimeException {
    public WrongCheckPassword(String message) {
        super(message);
    }
}
