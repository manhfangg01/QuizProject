package com.quiz.learning.Demo.controller.admin;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.quiz.learning.Demo.domain.filterCriteria.admin.QuestionFilter;
import com.quiz.learning.Demo.domain.request.admin.question.CreateQuestionRequest;
import com.quiz.learning.Demo.domain.request.admin.question.UpdateQuestionRequest;
import com.quiz.learning.Demo.domain.request.admin.user.CreateUserRequest;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchQuestionPaginationDTO;
import com.quiz.learning.Demo.domain.restResponse.ApiMessage;
import com.quiz.learning.Demo.service.admin.AdminQuestionService;
import com.quiz.learning.Demo.service.admin.relationServices.AdminQuestionRelationQuiz;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api")
public class AdminQuestionController {
    private final AdminQuestionService questionService;
    private final AdminQuestionRelationQuiz adminQuestionRelationQuiz;

    public AdminQuestionController(AdminQuestionService questionService,
            AdminQuestionRelationQuiz adminQuestionRelationQuiz) {
        this.questionService = questionService;
        this.adminQuestionRelationQuiz = adminQuestionRelationQuiz;
    }

    @GetMapping("/admin/questions/fetch")
    @ApiMessage("fetch all questions")
    public ResponseEntity<FetchQuestionPaginationDTO> fetchAllQuestions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            @ModelAttribute QuestionFilter filterCriteria) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.questionService.handleFetchAllQuestions(page, size, sortBy, order, filterCriteria));
    }

    @GetMapping("/admin/questions/fetch/{id}")
    @ApiMessage("fetch a question")
    public ResponseEntity<FetchAdminDTO.FetchQuestionDTO> fetchOne(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.questionService.handleFetchOneQuestion(id));
    }

    @PostMapping("/admin/questions/create")
    @ApiMessage("create a question")
    public ResponseEntity<FetchAdminDTO.FetchQuestionDTO> create(
            @Valid @RequestPart("createQuestionRequest") CreateQuestionRequest ques,
            @RequestPart(value = "questionImage", required = false) MultipartFile questionImage) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.questionService.handleCreateQuestion(ques, questionImage));
    }

    @PutMapping("/admin/questions/update")
    @ApiMessage("update a question")
    public ResponseEntity<FetchAdminDTO.FetchQuestionDTO> update(
            @Valid @RequestPart("updateQuestionRequest") UpdateQuestionRequest ques,
            @RequestPart(value = "questionImage", required = false) MultipartFile questionImage) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.questionService.handleUpdateQuestion(ques, questionImage));

    }

    @DeleteMapping("/admin/questions/delete/{id}")
    @ApiMessage("delete a question")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.adminQuestionRelationQuiz.handleDeleteQuestion(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
