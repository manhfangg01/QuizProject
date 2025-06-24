package com.quiz.learning.Demo.util.error.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.quiz.learning.Demo.domain.restResponse.RestResponse;
import com.quiz.learning.Demo.util.error.DuplicatedObjectException;
import com.quiz.learning.Demo.util.error.NullObjectException;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = DuplicatedObjectException.class)
    public ResponseEntity<RestResponse<Object>> handleIdException(DuplicatedObjectException idException) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(idException.getMessage());
        res.setMessage("DuplicatedObjectException");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = NullObjectException.class)
    public ResponseEntity<RestResponse<Object>> handleIdException(NullObjectException idException) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(idException.getMessage());
        res.setMessage("NullObjectException");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = ObjectNotFound.class)
    public ResponseEntity<RestResponse<Object>> handleIdException(ObjectNotFound idException) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(idException.getMessage());
        res.setMessage("ObjectNotFound");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

}
