package com.quiz.learning.Demo.controller.client;

import org.springframework.web.bind.annotation.RestController;

import com.quiz.learning.Demo.domain.response.client.ClientPaginationResultHistory;
import com.quiz.learning.Demo.domain.response.client.ResponseClientQuizResultDTO;
import com.quiz.learning.Demo.domain.response.client.result.ClientDetailResultDTO;
import com.quiz.learning.Demo.service.client.ClientResultService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
public class ClientResultController {
    private final ClientResultService clientResultService;

    public ClientResultController(ClientResultService clientResultService) {
        this.clientResultService = clientResultService;
    }

    @GetMapping("/client/results/{quizId}")
    public ResponseEntity<ResponseClientQuizResultDTO> getClientQuizResult(
            @PathVariable("quizId") Long quizId) {

        ResponseClientQuizResultDTO dto = clientResultService.getClientQuizResult(quizId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/client/results/history/{userId}")
    public ResponseEntity<ClientPaginationResultHistory> getHistory(
            @PathVariable("userId") Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String order) {
        return ResponseEntity.ok(this.clientResultService.handleFetchHistory(page, size, sortBy, order, userId));
    }

    @GetMapping("/client/results/detail/{resultId}")
    public ResponseEntity<ClientDetailResultDTO> getDetail(@PathVariable("resultId") Long resultId) {
        return ResponseEntity.ok(this.clientResultService.handleFetchDetailResult(resultId));
    }

    @GetMapping("/client/results/statistics")
    public String getStatistics(@RequestParam String param) {
        return new String();
    }

}
