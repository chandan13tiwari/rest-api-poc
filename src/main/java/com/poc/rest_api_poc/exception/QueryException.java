package com.poc.rest_api_poc.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryException extends RuntimeException {
    public QueryException(String message) {
        super(message);
    }

    public QueryException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public QueryException(RuntimeException exception) {
        super(exception);
    }
}