package com.quiz.learning.Demo.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.RestController;

import com.quiz.learning.Demo.domain.Quiz;
import com.quiz.learning.Demo.domain.request.admin.quiz.CreateQuizRequest;
import com.quiz.learning.Demo.domain.request.admin.quiz.UpdateQuizRequest;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.domain.response.client.DisplayClientDTO;
import com.quiz.learning.Demo.domain.response.client.FetchClientDTO;
import com.quiz.learning.Demo.domain.restResponse.ApiMessage;
import com.quiz.learning.Demo.service.admin.AdminQuizService;
import com.quiz.learning.Demo.util.error.DuplicatedObjectException;
import com.quiz.learning.Demo.util.error.NullObjectException;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

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
    @GetMapping("admin/quizzies/fetch")
    @ApiMessage("Truy vấn tất cả Quizzies")
    public ResponseEntity<List<FetchAdminDTO.FetchQuizDTO>> fetchAll() {
        return ResponseEntity.ok().body(this.quizService.handleFetchAllQuizzies());
    }

    @GetMapping("admin/quizzies/fetch/{id}")
    @ApiMessage("Truy vấn 1 Quiz")
    public ResponseEntity<FetchAdminDTO.FetchQuizDTO> fetchOne(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(this.quizService.handleFetchQuizById(id));
    }

    @PostMapping("admin/quizzies/create")
    @ApiMessage("Tạo 1 Quiz")
    public ResponseEntity<FetchAdminDTO.FetchQuizDTO> create(@RequestBody CreateQuizRequest quiz) {
        return ResponseEntity.ok().body(this.quizService.handleCreateQuiz(quiz));
    }

    @PutMapping("admin/quizzies/update")
    @ApiMessage("Cập nhật 1 Quiz")
    public ResponseEntity<FetchAdminDTO.FetchQuizDTO> updateQuiz(@RequestBody UpdateQuizRequest updatedQuiz) {

        return ResponseEntity.ok().body(this.quizService.handleUpdateQuiz(updatedQuiz));
    }

    @DeleteMapping("admin/quizzies/delete/{id}")
    @ApiMessage("Xóa 1 Quiz")
    public ResponseEntity<Void> deleteQuiz(@PathVariable("id") long id) {
        this.quizService.handleDeleteQuiz(id);
        return ResponseEntity.ok().body(null);
    }

}
