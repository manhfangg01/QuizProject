package com.quiz.learning.Demo.domain.request.admin.user;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

    @NotBlank(message = "Họ tên không được để trống")
    @Size(min = 3, max = 100, message = "Họ tên phải có độ dài từ 3 đến 100 ký tự")
    private String fullName;

    @Email(message = "Định dạng email không hợp lệ")
    @NotBlank(message = "Email không được để trống")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 100, message = "Mật khẩu phải từ 6 đến 100 ký tự")
    private String password;

    @NotNull(message = "Vai trò không được để trống")
    private Long roleId;
}
