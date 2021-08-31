package com.employees.errorHandling;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice
public class RestErrorHandler {

    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";

        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }
    @ExceptionHandler({ EmployeeNotFoundException.class })
    public ResponseEntity<Object> handleEmployeeNotFound(
            EmployeeNotFoundException ex, WebRequest request) {
        ApiError apiError =
                new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }
    @ExceptionHandler({ DepartmentNotFoundException.class })
    public ResponseEntity<Object> handleDepartmentNotFound(
            DepartmentNotFoundException ex, WebRequest request) {
        ApiError apiError =
                new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ TeamNotFoundException.class })
    public ResponseEntity<Object> handleTeamNotFound(
            TeamNotFoundException ex, WebRequest request) {
        ApiError apiError =
                new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }




    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());

    }

    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(
                " method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

        ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED,
                ex.getLocalizedMessage());
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t + ", "));

        ApiError apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                ex.getLocalizedMessage());
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }



    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        log.error("exception",ex);
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage());
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }
    }
