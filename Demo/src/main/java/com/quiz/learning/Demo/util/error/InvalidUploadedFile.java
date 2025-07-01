package com.quiz.learning.Demo.util.error;

public class InvalidUploadedFile extends RuntimeException {
    public InvalidUploadedFile(String message) {
        super(message);
    }

}
