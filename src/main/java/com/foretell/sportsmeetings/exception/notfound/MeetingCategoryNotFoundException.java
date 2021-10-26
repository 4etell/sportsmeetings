package com.foretell.sportsmeetings.exception.notfound;

public class MeetingCategoryNotFoundException extends RuntimeException {
    public MeetingCategoryNotFoundException(String message) {
        super(message);
    }
}
