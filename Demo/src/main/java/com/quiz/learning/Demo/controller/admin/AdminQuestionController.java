package com.quiz.learning.Demo.controller.admin;

import org.springframework.web.bind.annotation.RestController;

import com.quiz.learning.Demo.domain.Question;
import com.quiz.learning.Demo.domain.request.admin.question.CreateQuestionRequest;
import com.quiz.learning.Demo.domain.request.admin.question.UpdateQuestionRequest;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.service.admin.AdminQuestionService;
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
public class AdminQuestionController {
    private final AdminQuestionService questionService;

    public AdminQuestionController(AdminQuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/admin/questions/fetch")
    public ResponseEntity<List<FetchAdminDTO.FetchQuestionDTO>> fetchAllQuestions() {
        return ResponseEntity.status(HttpStatus.OK).body(this.questionService.handleFetchAllQuestions());
    }

    @GetMapping("/admin/questions/fetch/{id}")
    public ResponseEntity<FetchAdminDTO.FetchQuestionDTO> fetchOne(@PathVariable("id") long id) throws ObjectNotFound {
        return ResponseEntity.status(HttpStatus.OK).body(this.questionService.handleFetchOneQuestion(id));
    }

    @PostMapping("/admin/questions/create")
    public ResponseEntity<FetchAdminDTO.FetchQuestionDTO> create(@RequestBody CreateQuestionRequest ques)
            throws NullObjectException, DuplicatedObjectException {
        return ResponseEntity.status(HttpStatus.OK).body(this.questionService.handleCreateQuestion(ques));
    }

    @PutMapping("/admin/questions/update")
    public ResponseEntity<FetchAdminDTO.FetchQuestionDTO> update(@RequestBody UpdateQuestionRequest updatedQues)
            throws ObjectNotFound, DuplicatedObjectException {
        return ResponseEntity.status(HttpStatus.OK).body(this.questionService.handleUpdateQuestion(updatedQues));

    }

    @DeleteMapping("/admin/questions/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) throws ObjectNotFound {
        this.questionService.handleDeleteQuestion(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
