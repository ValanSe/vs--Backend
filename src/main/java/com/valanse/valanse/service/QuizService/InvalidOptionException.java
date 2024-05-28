package com.valanse.valanse.service.QuizService;

public class InvalidOptionException extends RuntimeException {
    public InvalidOptionException(String message, Throwable cause) {
        super(message, cause);
    }
}