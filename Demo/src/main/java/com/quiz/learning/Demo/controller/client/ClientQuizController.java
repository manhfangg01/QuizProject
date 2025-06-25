package com.quiz.learning.Demo.controller.client;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.learning.Demo.domain.Quiz;
import com.quiz.learning.Demo.domain.response.client.DisplayClientDTO;
import com.quiz.learning.Demo.domain.response.client.FetchClientDTO;
import com.quiz.learning.Demo.domain.restResponse.ApiMessage;
import com.quiz.learning.Demo.service.client.ClientQuizService;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

@RestController
public class ClientQuizController {
    private final ClientQuizService quizService;

    public ClientQuizController(ClientQuizService quizService) {
        this.quizService = quizService;
    }

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
    public ResponseEntity<FetchClientDTO.QuizClientDTO> ClientfetchOne(@PathVariable("id") long id)
            throws ObjectNotFound {
        return ResponseEntity.ok().body(this.quizService.handleFetchQuizById(id));
    }

}
