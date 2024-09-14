package com.poc.rest_api_poc.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class EmployeeException extends RuntimeException {

    private HttpStatus httpStatusCode;
    public EmployeeException(String message, HttpStatus httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public EmployeeException(String message, Throwable throwable, HttpStatus httpStatusCode) {
        super(message, throwable);
        this.httpStatusCode = httpStatusCode;
    }

    public EmployeeException(RuntimeException exception, HttpStatus httpStatusCode) {
        super(exception);
        this.httpStatusCode = httpStatusCode;
    }
}
