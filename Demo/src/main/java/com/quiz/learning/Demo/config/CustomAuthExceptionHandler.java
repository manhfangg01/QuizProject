package com.quiz.learning.Demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.learning.Demo.domain.restResponse.RestResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {
    // Xử lý lỗi dưới tầng filter

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Xử lý lỗi 401 - chưa đăng nhập hoặc token sai
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        if (response.isCommitted()) {
            return; // response đã được ghi rồi, không nên ghi đè nữa
        }

        RestResponse<Object> errorResponse = new RestResponse<>();
        errorResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setError("Unauthorized");
        errorResponse.setMessage("Bạn chưa đăng nhập hoặc token không hợp lệ");

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    // Xử lý lỗi 403 - đã đăng nhập nhưng không đủ quyền
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {
        if (response.isCommitted()) {
            return; // đã ghi xong response rồi
        }

        RestResponse<Object> errorResponse = new RestResponse<>();
        errorResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
        errorResponse.setError("Forbidden");
        errorResponse.setMessage("Bạn không có quyền truy cập tài nguyên này");

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

}