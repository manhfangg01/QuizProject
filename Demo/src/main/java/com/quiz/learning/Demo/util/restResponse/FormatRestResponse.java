package com.quiz.learning.Demo.util.restResponse;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.quiz.learning.Demo.domain.restResponse.ApiMessage;
import com.quiz.learning.Demo.domain.restResponse.RestResponse;

@RestControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();

        if (body instanceof RestResponse || body instanceof String || body instanceof Resource) {
            return body;
        }

        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(status);
        res.setData(body);
        res.setMessage(getMessage(returnType, status));

        return res;
    }

    private String getMessage(MethodParameter returnType, int status) {
        if (status >= 400) {
            return "CALL API FAILED";
        }

        ApiMessage annotation = returnType.getMethodAnnotation(ApiMessage.class);
        return (annotation != null) ? annotation.value() : "CALL API SUCCESS";
    }
}
