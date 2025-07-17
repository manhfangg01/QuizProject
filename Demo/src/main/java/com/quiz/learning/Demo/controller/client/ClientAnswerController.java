package com.quiz.learning.Demo.controller.client;

import org.springframework.web.bind.annotation.RestController;

import com.quiz.learning.Demo.domain.response.client.result.ClientDetailResultDTO.DetailAnswer;
import com.quiz.learning.Demo.service.client.ClientAnswerService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api")
public class ClientAnswerController {
    private final ClientAnswerService clientAnswerService;

    public ClientAnswerController(ClientAnswerService clientAnswerService) {
        this.clientAnswerService = clientAnswerService;
    }

    @GetMapping("/client/answers/{answerId}")
    public ResponseEntity<DetailAnswer> getDetailAnswer(@PathVariable("answerId") Long answerId) {
        return ResponseEntity.ok(this.clientAnswerService.handleFetchDetailAnswer(answerId));
    }

}
