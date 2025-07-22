package com.hien.back_end_app.config.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hien.back_end_app.dto.response.ApiErrorResponse;
import com.hien.back_end_app.utils.enums.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        OAuth2AuthenticationException ex = (OAuth2AuthenticationException) exception;
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .status(Integer.parseInt(ex.getError().getErrorCode()))
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .error(ex.getError().getDescription())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(apiErrorResponse));
    }
}
