package com.quiz.learning.Demo.domain.request.admin.user;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    @NotNull(message = "ID người dùng không được để trống")
    private Long userId;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email không được để trống")
    private String email;

    private String password; // optional – nếu cập nhật

    private Set<Long> roleIds; // cập nhật vai trò

}
