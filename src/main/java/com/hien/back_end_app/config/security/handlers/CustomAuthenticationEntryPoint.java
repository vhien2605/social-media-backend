package com.hien.back_end_app.config.security.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hien.back_end_app.dto.response.ApiErrorResponse;
import com.hien.back_end_app.exceptions.AuthException;
import com.hien.back_end_app.utils.enums.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("-----------------------------------authentication entry point start------------------------------------");
        AuthException exception = (AuthException) authException;
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .status(exception.getErrorCode().getCode())
                .message(exception.getErrorCode().getMessage())
                .path(request.getRequestURI())
                .error(exception.getErrorCode().name())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        // convert object to string
        response.getWriter().write(objectMapper.writeValueAsString(apiErrorResponse));
    }
}
