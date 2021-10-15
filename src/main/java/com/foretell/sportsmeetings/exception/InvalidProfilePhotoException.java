package com.foretell.sportsmeetings.exception;

import java.io.IOException;

public class InvalidProfilePhotoException extends IOException {
    public InvalidProfilePhotoException(String message) {
        super(message);
    }
}
