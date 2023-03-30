package com.cashew.budgetservice.exceptions;

public class FetchDataException extends RuntimeException {
    public FetchDataException(String message) {
        super(message);
    }

    public FetchDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
