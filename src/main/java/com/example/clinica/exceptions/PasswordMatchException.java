package com.example.clinica.exceptions;

public class PasswordMatchException extends RuntimeException {

    public PasswordMatchException(String message) {
        super(message);
    }

}
