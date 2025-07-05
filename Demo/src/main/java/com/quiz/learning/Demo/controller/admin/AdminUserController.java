package com.quiz.learning.Demo.controller.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.quiz.learning.Demo.domain.request.admin.user.CreateUserRequest;
import com.quiz.learning.Demo.domain.request.admin.user.UpdateUserRequest;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO.FetchUserPaginationDTO;
import com.quiz.learning.Demo.domain.restResponse.ApiMessage;
import com.quiz.learning.Demo.service.admin.AdminUserService;
import com.quiz.learning.Demo.service.admin.relationServices.AdminResultRelationUser;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api")
public class AdminUserController {
    private final AdminUserService userService;
    private final AdminResultRelationUser adminResultRelationUser;

    public AdminUserController(AdminUserService userService, AdminResultRelationUser adminResultRelationUser) {
        this.userService = userService;
        this.adminResultRelationUser = adminResultRelationUser;
    }

    @GetMapping("/admin/users/fetch")
    @ApiMessage("fetch all users")
    public ResponseEntity<FetchUserPaginationDTO> fetch(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.userService.handleFetchAllUsers(page, size, sortBy, order));
    }

    @GetMapping("/admin/users/fetch/{id}")
    @ApiMessage("fetch one user")
    public ResponseEntity<FetchAdminDTO.FetchUserDTO> fetchOne(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleFetchOneUser(id));
    }

    @PostMapping("/admin/users/create")
    @ApiMessage("create a user")
    public ResponseEntity<FetchAdminDTO.FetchUserDTO> create(
            @Valid @RequestPart("createUserRequest") CreateUserRequest newUser,
            @RequestPart(value = "userAvatar", required = false) MultipartFile userAvatar) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleCreateUser(newUser, userAvatar));
    }

    @PostMapping("/admin/users/update")
    public ResponseEntity<FetchAdminDTO.FetchUserDTO> update(
            @RequestPart("updateUserRequest") @Valid UpdateUserRequest updatedUser,
            @RequestPart(value = "userAvatar", required = false) MultipartFile userAvatar) {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.userService.handleUpdateUser(updatedUser, userAvatar));
    }

    @DeleteMapping("/admin/users/delete/{id}")
    @ApiMessage("delete a user")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        this.adminResultRelationUser.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
