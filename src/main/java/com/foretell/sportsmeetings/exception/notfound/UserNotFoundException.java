package com.foretell.sportsmeetings.exception.notfound;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
