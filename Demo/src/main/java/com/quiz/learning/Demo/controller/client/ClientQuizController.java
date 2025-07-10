package com.quiz.learning.Demo.controller.client;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.learning.Demo.domain.filterCriteria.admin.QuizFilter;
import com.quiz.learning.Demo.domain.filterCriteria.client.QuizClientFilter;
import com.quiz.learning.Demo.domain.response.client.FetchClientDTO;
import com.quiz.learning.Demo.domain.response.client.RequestSubmissionDTO;
import com.quiz.learning.Demo.domain.response.client.ResponseSubmissionDTO;
import com.quiz.learning.Demo.domain.response.client.FetchClientDTO.QuizClientDTO;
import com.quiz.learning.Demo.domain.response.client.FetchClientDTO.QuizClientPaginationDTO;
import com.quiz.learning.Demo.domain.response.client.FetchClientDTO.QuizClientPlayDTO;
import com.quiz.learning.Demo.domain.restResponse.ApiMessage;
import com.quiz.learning.Demo.service.client.ClientQuizService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class ClientQuizController {
    private final ClientQuizService quizService;

    public ClientQuizController(ClientQuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/client/quizzes/fetch")
    @ApiMessage("Truy cập các quizzes công khai")
    public ResponseEntity<QuizClientPaginationDTO> fetchClientQuizzes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            @ModelAttribute QuizClientFilter filterCriteria) {
        return ResponseEntity.ok().body(this.quizService.handleFetchQuizzes(page, size, sortBy, order, filterCriteria));
    }

    @GetMapping("/client/quizzes/display/{id}")
    @ApiMessage("Hiển thị làm bài Quiz")
    public ResponseEntity<QuizClientPlayDTO> displayQuiz(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(this.quizService.handleDisplayQuiz(id));
    }

    @PostMapping("/client/quizzes/submit")
    public ResponseEntity<ResponseSubmissionDTO> postMethodName(@RequestBody RequestSubmissionDTO submit) {
        return ResponseEntity.status(HttpStatus.OK).body(this.quizService.handleSubmitAnswer(submit));
    }

    // todo
    // POST /api/client/quizzies/save-progress Lưu tạm câu trả lời Client
    // GET /api/client/results/{quizId} Xem kết quả 1 quiz Client
    // GET /api/client/results/history Xem lịch sử làm bài Client
    // GET /api/client/statistics Xem thống kê tổng thể Client
    // POST /api/client/quizzies/exit Ghi nhận thoát quiz Client

}
