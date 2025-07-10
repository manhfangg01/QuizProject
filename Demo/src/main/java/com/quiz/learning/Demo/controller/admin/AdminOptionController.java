package com.quiz.learning.Demo.controller.admin;

import org.springframework.web.bind.annotation.RestController;

import com.quiz.learning.Demo.domain.filterCriteria.admin.OptionFilter;
import com.quiz.learning.Demo.domain.request.admin.option.CreateOptionRequest;
import com.quiz.learning.Demo.domain.request.admin.option.UpdateOptionRequest;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchOptionDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchOptionPaginationDTO;
import com.quiz.learning.Demo.domain.restResponse.ApiMessage;
import com.quiz.learning.Demo.service.admin.AdminOptionService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @ApiMessage("fetch all options")
    public ResponseEntity<FetchOptionPaginationDTO> fetch(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            @ModelAttribute OptionFilter filterCriteria) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.optionService.handleFetchAllOptions(page, size, sortBy, order, filterCriteria));
    }

    @PostMapping("/admin/options/multi-fetch")
    public ResponseEntity<List<FetchOptionDTO>> fetchMultiOption(@RequestBody List<Long> optionIds) { // Post mới được
                                                                                                      // truyền
                                                                                                      // requestBody,
                                                                                                      // còn Get là chỉ
                                                                                                      // lấy từ param
                                                                                                      // thôi
        return ResponseEntity.ok(this.optionService.handleGetOptions(optionIds));
    }

    @GetMapping("/admin/options/fetch-available")
    @ApiMessage("fetch availables")
    public ResponseEntity<FetchOptionPaginationDTO> fetchAvailableOptions(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            @ModelAttribute OptionFilter filterCriteria) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.optionService.handleFetchAllAvailableOptions(page, size, sortBy, order, filterCriteria));
    }

    @GetMapping("/admin/options/fetch/{id}")
    @ApiMessage("fetch an option")
    public ResponseEntity<FetchAdminDTO.FetchOptionDTO> fetchOne(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.optionService.handleFetchOneOption(id));
    }

    @PostMapping("/admin/options/create")
    @ApiMessage("create an option")
    public ResponseEntity<FetchAdminDTO.FetchOptionDTO> create(@Valid @RequestBody CreateOptionRequest newOption) {
        return ResponseEntity.status(HttpStatus.OK).body(this.optionService.handleCreateOption(newOption));
    }

    @PutMapping("/admin/options/update")
    @ApiMessage("update an option")
    public ResponseEntity<FetchAdminDTO.FetchOptionDTO> update(@Valid @RequestBody UpdateOptionRequest updatedOption) {
        return ResponseEntity.status(HttpStatus.OK).body(this.optionService.handleUpdateOption(updatedOption));
    }

    @DeleteMapping("/admin/options/delete/{id}")
    @ApiMessage("delete an option")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        this.optionService.handleDeleteOption(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
