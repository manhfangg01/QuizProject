package com.quiz.learning.Demo.controller.admin;

import org.springframework.web.bind.annotation.RestController;

import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.service.admin.AdminResultService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
public class AdminResultController {
    private final AdminResultService resultService;

    public AdminResultController(AdminResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping("/admin/results/fetch")
    public ResponseEntity<List<FetchAdminDTO.FetchResultDTO>> fetch() {
        return ResponseEntity.status(HttpStatus.OK).body(this.resultService.handleFetchAllResults());
    }

    @GetMapping("/admin/results/fetch/{id}")
    public ResponseEntity<FetchAdminDTO.FetchResultDTO> fetchOne(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.resultService.handleFetchOneResult(id));
    }

    @GetMapping("/admin/results/user/fetch/{id}")
    public ResponseEntity<List<FetchAdminDTO.FetchResultDTO>> getMethodName(@PathVariable("id") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(this.resultService.handleFetchResultByUserId(userId));
    }

}
