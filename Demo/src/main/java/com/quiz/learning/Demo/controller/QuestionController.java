package com.quiz.learning.Demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.quiz.learning.Demo.domain.Question;
import com.quiz.learning.Demo.service.QuestionService;
import com.quiz.learning.Demo.util.error.DuplicatedObjectException;
import com.quiz.learning.Demo.util.error.NullObjectException;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

import java.util.List;

import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/admin/questions/fetch")
    public ResponseEntity<List<Question>> fetchAllQuestions() {
        return ResponseEntity.status(HttpStatus.OK).body(this.questionService.handleFetchAllQuestions());
    }

    @GetMapping("/admin/questions/fetch/{id}")
    public ResponseEntity<Question> fetchOne(@PathVariable("id") long id) throws ObjectNotFound {
        return ResponseEntity.status(HttpStatus.OK).body(this.questionService.handleFetchOneQuestion(id));
    }

    @PostMapping("/admin/questions/create")
    public ResponseEntity<Question> create(@RequestBody Question ques)
            throws NullObjectException, DuplicatedObjectException {
        return ResponseEntity.status(HttpStatus.OK).body(this.questionService.handleCreateQuestion(ques));
    }

    @PutMapping("/admin/questions/update")
    public ResponseEntity<Question> update(@RequestBody Question updatedQues)
            throws ObjectNotFound, DuplicatedObjectException {
        return ResponseEntity.status(HttpStatus.OK).body(this.questionService.handleUpdateQuestion(updatedQues));

    }

    @DeleteMapping("/admin/questions/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) throws ObjectNotFound {
        this.questionService.handleDeleteQuestion(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
