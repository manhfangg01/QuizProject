package com.quiz.learning.Demo.controller.admin;

import org.springframework.web.bind.annotation.RestController;

import com.quiz.learning.Demo.domain.request.admin.question.CreateQuestionRequest;
import com.quiz.learning.Demo.domain.request.admin.question.UpdateQuestionRequest;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.domain.restResponse.ApiMessage;
import com.quiz.learning.Demo.service.admin.AdminQuestionService;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @ApiMessage("fetch all questions")
    public ResponseEntity<List<FetchAdminDTO.FetchQuestionDTO>> fetchAllQuestions() {
        return ResponseEntity.status(HttpStatus.OK).body(this.questionService.handleFetchAllQuestions());
    }

    @GetMapping("/admin/questions/fetch/{id}")
    @ApiMessage("fetch a question")
    public ResponseEntity<FetchAdminDTO.FetchQuestionDTO> fetchOne(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.questionService.handleFetchOneQuestion(id));
    }

    @PostMapping("/admin/questions/create")
    @ApiMessage("create a question")
    public ResponseEntity<FetchAdminDTO.FetchQuestionDTO> create(@RequestBody CreateQuestionRequest ques) {
        return ResponseEntity.status(HttpStatus.OK).body(this.questionService.handleCreateQuestion(ques));
    }

    @PutMapping("/admin/questions/update")
    @ApiMessage("update a question")
    public ResponseEntity<FetchAdminDTO.FetchQuestionDTO> update(@RequestBody UpdateQuestionRequest updatedQues) {
        return ResponseEntity.status(HttpStatus.OK).body(this.questionService.handleUpdateQuestion(updatedQues));

    }

    @DeleteMapping("/admin/questions/delete/{id}")
    @ApiMessage("delete a question")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.questionService.handleDeleteQuestion(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
