package com.valanse.valanse.security.filter;

public class JwtException extends RuntimeException {

    public JwtException(String message) {
        super(message);
    }
}
