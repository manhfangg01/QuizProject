package com.quiz.learning.Demo.util.error;

public class EmailSendingException extends RuntimeException {
    public EmailSendingException(String message) {
        super(message);
    }

}
