package com.quiz.learning.Demo.util.error;

public class ResourceNotExisted extends RuntimeException {
    public ResourceNotExisted(String message) {
        super(message);
    }

}
