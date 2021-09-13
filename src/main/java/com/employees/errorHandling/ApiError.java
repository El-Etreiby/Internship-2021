package com.employees.errorHandling;

import org.springframework.http.HttpStatus;

public class ApiError {

    private HttpStatus status;

    public ApiError(HttpStatus status, String message) {
        super();
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }


}
