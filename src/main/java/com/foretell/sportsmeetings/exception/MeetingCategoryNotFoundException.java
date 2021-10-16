package com.foretell.sportsmeetings.exception;

public class MeetingCategoryNotFoundException extends RuntimeException {
    public MeetingCategoryNotFoundException(String message) {
        super(message);
    }
}
