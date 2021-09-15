package com.employees.errorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MissingInputException extends RuntimeException {

    public MissingInputException(String message) {
        super(message);
    }
}


