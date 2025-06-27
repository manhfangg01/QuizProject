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

    @GetMapping("/client/quizzes/fetch")
    @ApiMessage("Truy cập các quizzes công khai")
    public ResponseEntity<List<FetchClientDTO.QuizClientDTO>> Clientfetchquizzes() {
        return ResponseEntity.ok().body(this.quizService.handleClientFetchquizzes());
    }

    @GetMapping("/client/quizzes/display/{id}")
    @ApiMessage("Hiển thị làm bài Quiz")
    public ResponseEntity<DisplayClientDTO.QuizPlayDTO> ClientdisplayQuiz(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(this.quizService.handleClientDisplayQuiz(id));
    }

    @GetMapping("/client/quizzes/fetch/{id}")
    public ResponseEntity<FetchClientDTO.QuizClientDTO> ClientfetchOne(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(this.quizService.handleFetchQuizById(id));
    }

}
