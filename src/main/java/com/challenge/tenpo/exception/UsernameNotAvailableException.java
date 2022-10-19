package com.challenge.tenpo.exception;

public class UsernameNotAvailableException extends IllegalArgumentException {

    public UsernameNotAvailableException(String message) {
        super(message);
    }
}
