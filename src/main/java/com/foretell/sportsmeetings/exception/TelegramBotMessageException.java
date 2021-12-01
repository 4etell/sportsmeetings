package com.foretell.sportsmeetings.exception;

public class TelegramBotMessageException extends RuntimeException {
    public TelegramBotMessageException(String message) {
        super(message);
    }
}
