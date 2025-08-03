package com.hien.back_end_app.exceptions;

import com.hien.back_end_app.dto.response.ApiErrorResponse;
import com.hien.back_end_app.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandlerAdvice {
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({AppException.class})
    public ApiResponse handleAppException(AppException e, WebRequest request) {
        log.info("---------------------------Application exception handler start---------------------------");
        String error = e.getMessage();
        return ApiErrorResponse.builder()
                .status(e.getErrorCode().getCode())
                .message(e.getErrorCode().getMessage())
                .error(e.getErrorCode().name())
                .path(request.getDescription(false))
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({AuthException.class})
    public ApiResponse handleAuthException(AuthException e, WebRequest request) {
        log.info("---------------------------Auth exception handler start---------------------------");
        String error = e.getMessage();
        return ApiErrorResponse.builder()
                .status(e.getErrorCode().getCode())
                .message(e.getErrorCode().getMessage())
                .error(e.getErrorCode().name())
                .path(request.getDescription(false))
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({IOException.class})
    public ApiResponse handleIOCloudinaryEx(IOException e, WebRequest request) {
        log.info("---------------------------IO exception handler start---------------------------");
        String error = e.getMessage();
        return ApiErrorResponse.builder()
                .status(400)
                .message(e.getMessage())
                .error("Cloudinary failed")
                .path(request.getDescription(false))
                .build();
    }


    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({Exception.class})
    public ApiResponse handleServerError(Exception e, WebRequest request) {
        log.info("---------------------------Server error 500 exception handler start---------------------------");
        String error = e.getMessage();
        return ApiErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .error(e.getMessage())
                .path(request.getDescription(false))
                .build();
    }
}
