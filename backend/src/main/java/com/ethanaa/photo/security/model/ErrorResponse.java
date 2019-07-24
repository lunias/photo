package com.ethanaa.photo.security.model;


import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;


public class ErrorResponse {

    private final String request;

    private final HttpStatus status;

    private final String message;

    private final ErrorCode errorCode;

    private final LocalDateTime timestamp;

    protected ErrorResponse(final String message, final ErrorCode errorCode,
                            final HttpStatus status, final String request) {

        this.request = request;
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public static ErrorResponse of(final String message, final ErrorCode errorCode,
                                   final HttpStatus status) {

        return new ErrorResponse(message, errorCode, status, null);
    }

    public static ErrorResponse of(final String message, final ErrorCode errorCode,
                                   final HttpStatus status, final NativeWebRequest nativeWebRequest) {

        String request = null;

        HttpServletRequest servletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        if (servletRequest != null) {
            request = servletRequest.getMethod() + " " + servletRequest.getRequestURI();
        }

        return new ErrorResponse(message, errorCode, status, request);
    }

    public String getRequest() {
        return request;
    }

    public Integer getStatus() {
        return status.value();
    }

    public String getMessage() {
        return message;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
