package com.quiz.learning.Demo.controller.client;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.quiz.learning.Demo.domain.request.admin.user.CreateUserRequest;
import com.quiz.learning.Demo.domain.request.client.user.ClientRequestUserUpdate;
import com.quiz.learning.Demo.domain.response.client.profile.ProfileDTO;
import com.quiz.learning.Demo.domain.response.client.user.ClientResponseUserUpdate;
import com.quiz.learning.Demo.service.client.ClientUserService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class ClientUserController {
    private final ClientUserService userService;

    public ClientUserController(ClientUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/client/users/profile/{id}")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.userService.handleFetchProfile(id));
    }

    @PostMapping("/client/users/update-profile")
    public ResponseEntity<ClientResponseUserUpdate> update(
            @Valid @RequestPart("updateUserRequest") ClientRequestUserUpdate updatedUser,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar) {

        return ResponseEntity.ok(this.userService.handleUpdate(updatedUser, avatar));
    }

}
