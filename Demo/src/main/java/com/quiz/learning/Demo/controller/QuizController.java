package com.quiz.learning.Demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.RestController;

import com.quiz.learning.Demo.domain.Quiz;
import com.quiz.learning.Demo.domain.response.DisplayClientDTO;
import com.quiz.learning.Demo.domain.response.FetchClientDTO;
import com.quiz.learning.Demo.domain.restResponse.ApiMessage;
import com.quiz.learning.Demo.repository.QuizRepository;
import com.quiz.learning.Demo.service.QuizService;
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
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
public class QuizController {
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    // Admin API
    @GetMapping("admin/quizzies/fetch")
    @ApiMessage("Truy vấn tất cả Quizzies")
    public ResponseEntity<List<Quiz>> fetchAll() {
        return ResponseEntity.ok().body(this.quizService.handleFetchAllQuizzies());
    }

    @GetMapping("admin/quizzies/fetch/{id}")
    @ApiMessage("Truy vấn 1 Quiz")
    public ResponseEntity<Quiz> fetchOne(@PathVariable("id") long id) throws ObjectNotFound {
        return ResponseEntity.ok().body(this.quizService.handleFetchQuizById(id));
    }

    @PostMapping("admin/quizzies/create")
    @ApiMessage("Tạo 1 Quiz")
    public ResponseEntity<Quiz> create(@RequestBody Quiz quiz) throws NullObjectException, DuplicatedObjectException {
        return ResponseEntity.ok().body(this.quizService.handleCreateQuiz(quiz));
    }

    @PutMapping("admin/quizzies/update")
    @ApiMessage("Cập nhật 1 Quiz")
    public ResponseEntity<Quiz> updateQuiz(@RequestBody Quiz updatedQuiz)
            throws ObjectNotFound, DuplicatedObjectException {

        return ResponseEntity.ok().body(this.quizService.handleUpdateQuiz(updatedQuiz));
    }

    @DeleteMapping("admin/quizzies/delete/{id}")
    @ApiMessage("Xóa 1 Quiz")
    public ResponseEntity<Void> deleteQuiz(@PathVariable("id") long id) throws ObjectNotFound {
        this.quizService.handleDeleteQuiz(id);
        return ResponseEntity.ok().body(null);
    }

    // Client API

    @GetMapping("/client/quizzies/fetch")
    @ApiMessage("Truy cập các Quizzies công khai")
    public ResponseEntity<List<FetchClientDTO.QuizClientDTO>> ClientfetchQuizzies() {
        return ResponseEntity.ok().body(this.quizService.handleClientFetchQuizzies());
    }

    @GetMapping("/client/quizzies/display/{id}")
    @ApiMessage("Hiển thị làm bài Quiz")
    public ResponseEntity<DisplayClientDTO.QuizPlayDTO> ClientdisplayQuiz(@PathVariable("id") long id)
            throws ObjectNotFound {
        return ResponseEntity.ok().body(this.quizService.handleClientDisplayQuiz(id));
    }

    @GetMapping("/client/quizzies/fetch/{id}")
    public ResponseEntity<Quiz> ClientfetchOne(@PathVariable("id") long id) throws ObjectNotFound {
        return ResponseEntity.ok().body(this.quizService.handleFetchQuizById(id));
    }

}
