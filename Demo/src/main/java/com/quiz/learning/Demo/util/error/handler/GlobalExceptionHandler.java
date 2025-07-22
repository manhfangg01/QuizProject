package com.quiz.learning.Demo.util.error.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.quiz.learning.Demo.domain.restResponse.RestResponse;
import com.quiz.learning.Demo.util.error.DuplicatedObjectException;
import com.quiz.learning.Demo.util.error.EmailSendingException;
import com.quiz.learning.Demo.util.error.EnvironmentValueError;
import com.quiz.learning.Demo.util.error.InvalidResetTokenException;
import com.quiz.learning.Demo.util.error.InvalidToken;
import com.quiz.learning.Demo.util.error.InvalidUploadedFile;
import com.quiz.learning.Demo.util.error.NullObjectException;
import com.quiz.learning.Demo.util.error.ObjectNotFound;
import com.quiz.learning.Demo.util.error.ResourceNotExisted;
import com.quiz.learning.Demo.util.error.WrongCheckPassword;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = DuplicatedObjectException.class)
    public ResponseEntity<RestResponse<Object>> handleDuplication(DuplicatedObjectException duplicatedObjectException) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(duplicatedObjectException.getMessage());
        res.setMessage(duplicatedObjectException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = NullObjectException.class)
    public ResponseEntity<RestResponse<Object>> handleNullPointer(NullObjectException nullObjectException) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(nullObjectException.getMessage());
        res.setMessage(nullObjectException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = ObjectNotFound.class)
    public ResponseEntity<RestResponse<Object>> handleNotExistent(ObjectNotFound objectNotFoundException) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(objectNotFoundException.getMessage());
        res.setMessage(objectNotFoundException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = WrongCheckPassword.class)
    public ResponseEntity<RestResponse<Object>> handleWrongCheckingPass(
            WrongCheckPassword wrongCheckPasswordException) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(wrongCheckPasswordException.getMessage());
        res.setMessage(wrongCheckPasswordException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = ResourceNotExisted.class)
    public ResponseEntity<RestResponse<Object>> handleResourceNotFound(ResourceNotExisted resourceNotExistedException) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(resourceNotExistedException.getMessage());
        res.setMessage(resourceNotExistedException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = InvalidToken.class)
    public ResponseEntity<RestResponse<Object>> handleInvalidToken(InvalidToken invalidTokenException) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(invalidTokenException.getMessage());
        res.setMessage(invalidTokenException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    // handle exceptions from Validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // hanlde exceptions from Uploading file
    @ExceptionHandler(InvalidUploadedFile.class)
    public ResponseEntity<RestResponse<Object>> handleUploadingException(InvalidUploadedFile ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getMessage());
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    // hanlde exceptions from sending email
    @ExceptionHandler(EmailSendingException.class)
    public ResponseEntity<RestResponse<Object>> handleEmailSendingException(EmailSendingException ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getMessage());
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    // hanlde exceptions from reseting password
    @ExceptionHandler(InvalidResetTokenException.class)
    public ResponseEntity<RestResponse<Object>> handleInvalidResetTokenException(InvalidResetTokenException ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getMessage());
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(EnvironmentValueError.class)
    public ResponseEntity<RestResponse<Object>> handleEnvironmentValueError(EnvironmentValueError ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("Biến môi trường bị rỗng");
        res.setMessage("Biến môi trường bị rỗng");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
