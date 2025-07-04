package com.quiz.learning.Demo.controller.admin;

import java.util.List;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.learning.Demo.domain.request.admin.quiz.CreateQuizRequest;
import com.quiz.learning.Demo.domain.request.admin.quiz.UpdateQuizRequest;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.domain.restResponse.ApiMessage;
import com.quiz.learning.Demo.service.admin.AdminQuizService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api")
public class AdminQuizController {
    private final AdminQuizService quizService;

    public AdminQuizController(AdminQuizService quizService) {
        this.quizService = quizService;
    }

    // Admin API
    @GetMapping("admin/quizzes/fetch")
    @ApiMessage("fetch all quizzes")
    public ResponseEntity<List<FetchAdminDTO.FetchQuizDTO>> fetchAll() {
        return ResponseEntity.ok().body(this.quizService.handleFetchAllquizzes());
    }

    @GetMapping("admin/quizzes/fetch/{id}")
    @ApiMessage("fetch a quiz")
    public ResponseEntity<FetchAdminDTO.FetchQuizDTO> fetchOne(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(this.quizService.handleFetchQuizById(id));
    }

    @PostMapping("admin/quizzes/create")
    @ApiMessage("create a question")
    public ResponseEntity<FetchAdminDTO.FetchQuizDTO> create(@Valid @RequestBody CreateQuizRequest quiz) {
        return ResponseEntity.ok().body(this.quizService.handleCreateQuiz(quiz));
    }

    @PutMapping("admin/quizzes/update")
    @ApiMessage("update a quiz")
    public ResponseEntity<FetchAdminDTO.FetchQuizDTO> updateQuiz(@Valid @RequestBody UpdateQuizRequest updatedQuiz) {

        return ResponseEntity.ok().body(this.quizService.handleUpdateQuiz(updatedQuiz));
    }

    @DeleteMapping("admin/quizzes/delete/{id}")
    @ApiMessage("delete a quiz")
    public ResponseEntity<Void> deleteQuiz(@PathVariable("id") Long id) {
        this.quizService.handleDeleteQuiz(id);
        return ResponseEntity.ok().body(null);
    }

}
