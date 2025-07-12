package com.quiz.learning.Demo.controller.admin;

import org.springframework.web.bind.annotation.RestController;

import com.quiz.learning.Demo.domain.filterCriteria.admin.ResultFilter;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchResultDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchResultPaginationDTO;
import com.quiz.learning.Demo.domain.restResponse.ApiMessage;
import com.quiz.learning.Demo.service.admin.AdminResultService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    @ApiMessage("fetch all results")
    public ResponseEntity<FetchResultPaginationDTO> fetch(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            @ModelAttribute ResultFilter filterCriteria) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.resultService.handleFetchAllResults(page, size, sortBy, order, filterCriteria));
    }

    @GetMapping("/admin/results/fetch/{id}")
    @ApiMessage("fetch result")
    public ResponseEntity<FetchResultDTO> fetchOne(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.resultService.handleGetResultById(id));
    }

    @GetMapping("/admin/results/quiz/fetch/{quizId}")
    @ApiMessage("fetch all results by quiz_id")
    public ResponseEntity<FetchResultPaginationDTO> fetchByQuiz(
            @PathVariable("quizId") Long id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            @ModelAttribute ResultFilter filterCriteria) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.resultService.handleFetchResultsByQuizId(id, page, size, sortBy, order, filterCriteria));
    }

    @GetMapping("/admin/results/user/fetch/{userId}")
    @ApiMessage("fetch all results by user_id")
    public ResponseEntity<FetchResultPaginationDTO> fetchByUser(
            @PathVariable("userId") Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            @ModelAttribute ResultFilter filterCriteria) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.resultService.handleFetchResultsByUserId(userId, page, size, sortBy, order, filterCriteria));
    }

    @DeleteMapping("/admin/results/delete/{id}")
    @ApiMessage(" delete result")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        this.resultService.handleDeleteResultById(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
