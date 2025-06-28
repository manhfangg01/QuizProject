package com.quiz.learning.Demo.controller.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.learning.Demo.domain.request.admin.user.CreateUserRequest;
import com.quiz.learning.Demo.domain.request.admin.user.UpdateUserRequest;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.domain.restResponse.ApiMessage;
import com.quiz.learning.Demo.service.admin.AdminUserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api")
public class AdminUserController {
    private final AdminUserService userService;

    public AdminUserController(AdminUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/users/fetch")
    @ApiMessage("fetch all users")
    public ResponseEntity<List<FetchAdminDTO.FetchUserDTO>> fetch() {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleFetchAllUsers());
    }

    @GetMapping("/admin/users/fetch/{id}")
    @ApiMessage("fetch one user")
    public ResponseEntity<FetchAdminDTO.FetchUserDTO> fetchOne(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleFetchOneUser(id));
    }

    @PostMapping("/admin/users/create")
    @ApiMessage("create a user")
    public ResponseEntity<FetchAdminDTO.FetchUserDTO> create(@Valid @RequestBody CreateUserRequest newUser) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleCreateUser(newUser));
    }

    @PutMapping("/admin/users/update")
    @ApiMessage("update a user")
    public ResponseEntity<FetchAdminDTO.FetchUserDTO> update(@Valid @RequestBody UpdateUserRequest updatedUser) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleUpdateUser(updatedUser));
    }

    @DeleteMapping("/admin/users/delete/{id}")
    @ApiMessage("delete a user")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
