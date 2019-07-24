package com.ethanaa.photo.security.controller.exception;

import com.ethanaa.photo.security.model.ErrorCode;
import com.ethanaa.photo.security.model.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    private static final HttpHeaders HEADERS;

    static {
        HEADERS = new HttpHeaders();
        HEADERS.setContentType(MediaType.APPLICATION_JSON);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<?> handleNotFoundException(NotFoundException e, NativeWebRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        ErrorResponse error = ErrorResponse.of(e.getMessage(), ErrorCode.GLOBAL, status, request);

        return handleExceptionInternal(e, error, HEADERS, status, request);
    }
}
