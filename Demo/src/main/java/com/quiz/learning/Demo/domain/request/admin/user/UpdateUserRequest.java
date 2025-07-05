package com.quiz.learning.Demo.domain.request.admin.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    @NotNull(message = "ID người dùng không được để trống")
    private Long userId;

    @NotBlank(message = "Họ tên không được để trống")
    @Size(min = 3, max = 100, message = "Họ tên phải có độ dài từ 3 đến 100 ký tự")
    private String fullName;

    @NotNull(message = "Vai trò không được để trống")
    private String role; // cập nhật vai trò
}
