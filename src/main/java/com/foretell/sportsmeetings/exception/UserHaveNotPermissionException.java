package com.foretell.sportsmeetings.exception;

public class UserHaveNotPermissionException extends RuntimeException {
    public UserHaveNotPermissionException(String message) {
        super(message);
    }
}
