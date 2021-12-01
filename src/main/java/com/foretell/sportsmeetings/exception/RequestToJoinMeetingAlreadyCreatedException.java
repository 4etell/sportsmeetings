package com.foretell.sportsmeetings.exception;

public class RequestToJoinMeetingAlreadyCreatedException extends RuntimeException {
    public RequestToJoinMeetingAlreadyCreatedException(String message) {
        super(message);
    }
}
