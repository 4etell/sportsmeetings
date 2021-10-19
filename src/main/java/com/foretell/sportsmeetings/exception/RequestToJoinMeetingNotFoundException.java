package com.foretell.sportsmeetings.exception;

public class RequestToJoinMeetingNotFoundException extends RuntimeException {
    public RequestToJoinMeetingNotFoundException(String message) {
        super(message);
    }
}
