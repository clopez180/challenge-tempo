package com.challenge.tenpo.exception;

public class InvalidUsernameException extends IllegalArgumentException {

    public InvalidUsernameException(String message) {
        super(message);
    }
}