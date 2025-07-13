package com.hien.back_end_app.config.security.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hien.back_end_app.dto.response.ApiErrorResponse;
import com.hien.back_end_app.utils.enums.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("-----------------------------------access denied handler start------------------------------------");
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .status(ErrorCode.ACCESS_DENIED.getCode())
                .message(ErrorCode.ACCESS_DENIED.getMessage())
                .path(request.getRequestURI())
                .error(ErrorCode.ACCESS_DENIED.name())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        // convert object to string
        response.getWriter().write(objectMapper.writeValueAsString(apiErrorResponse));
    }
}
