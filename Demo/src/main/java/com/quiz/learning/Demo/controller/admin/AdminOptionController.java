package com.quiz.learning.Demo.controller.admin;

import org.springframework.web.bind.annotation.RestController;

import com.quiz.learning.Demo.domain.request.admin.option.CreateOptionRequest;
import com.quiz.learning.Demo.domain.request.admin.option.UpdateOptionRequest;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.service.admin.AdminOptionService;
import com.quiz.learning.Demo.util.error.DuplicatedObjectException;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api")
public class AdminOptionController {
    private final AdminOptionService optionService;

    public AdminOptionController(AdminOptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/admin/options/fetch")
    public ResponseEntity<List<FetchAdminDTO.FetchOptionDTO>> fetch() {
        return ResponseEntity.status(HttpStatus.OK).body(this.optionService.handleFetchAllOptions());
    }

    @GetMapping("/admin/options/fetch/{id}")
    public ResponseEntity<FetchAdminDTO.FetchOptionDTO> fetchOne(@PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.optionService.handleFetchOneOption(id));
    }

    @PostMapping("/admin/options/create")
    public ResponseEntity<FetchAdminDTO.FetchOptionDTO> create(@RequestBody CreateOptionRequest newOption) {
        return ResponseEntity.status(HttpStatus.OK).body(this.optionService.handleCreateOption(newOption));
    }

    @PutMapping("/admin/options/update")
    public ResponseEntity<FetchAdminDTO.FetchOptionDTO> update(@RequestBody UpdateOptionRequest updatedOption) {
        return ResponseEntity.status(HttpStatus.OK).body(this.optionService.handleUpdateOption(updatedOption));
    }

    @GetMapping("/admin/options/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
