package com.valanse.valanse.exception;

public class CustomOAuth2AuthenticationException extends RuntimeException {

    public CustomOAuth2AuthenticationException(String message) {
        super(message);
    }

    public CustomOAuth2AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
