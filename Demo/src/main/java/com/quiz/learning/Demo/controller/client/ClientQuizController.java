package com.quiz.learning.Demo.controller.client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.learning.Demo.domain.filterCriteria.client.QuizClientFilter;
import com.quiz.learning.Demo.domain.request.client.RequestExitingQuiz;
import com.quiz.learning.Demo.domain.request.client.RequestSavingProgress;
import com.quiz.learning.Demo.domain.request.client.RequestSubmissionDTO;
import com.quiz.learning.Demo.domain.response.client.ResponseExitingQuiz;
import com.quiz.learning.Demo.domain.response.client.ResponseSavingProgress;
import com.quiz.learning.Demo.domain.response.client.ResponseSubmissionDTO;
import com.quiz.learning.Demo.domain.response.client.FetchClientDTO.QuizClientPaginationDTO;
import com.quiz.learning.Demo.domain.response.client.FetchClientDTO.QuizClientPlayDTO;
import com.quiz.learning.Demo.domain.restResponse.ApiMessage;
import com.quiz.learning.Demo.service.client.ClientQuizService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api")
public class ClientQuizController {
    private final ClientQuizService quizService;

    public ClientQuizController(ClientQuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/client/quizzes/fetch")
    @ApiMessage("Truy cập các quizzes công khai")
    public ResponseEntity<QuizClientPaginationDTO> fetchClientQuizzes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
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
    @ApiMessage("Nộp bài")
    public ResponseEntity<ResponseSubmissionDTO> postMethodName(@RequestBody RequestSubmissionDTO submit) {
        return ResponseEntity.status(HttpStatus.OK).body(this.quizService.handleSubmitAnswer(submit));
    }

    @PostMapping("/client/quizzes/save-progress")
    @ApiMessage("Lưu tiến độ ")
    public ResponseEntity<ResponseSavingProgress> saveProgress(@RequestBody RequestSavingProgress request) {

        return ResponseEntity.status(HttpStatus.OK).body(this.quizService.handleSaveProgress(request));
    }

    // GET /api/client/statistics Xem thống kê tổng thể Client

    @PostMapping("/client/quizzes/exit")
    @ApiMessage("Lưu tiến độ khi người dùng thoát chủ động")
    public ResponseEntity<ResponseExitingQuiz> saveProgressBeforeExiting(@RequestBody RequestExitingQuiz request) {

        return ResponseEntity.status(HttpStatus.OK).body(this.quizService.handleExitQuiz(request));
    }

}
