package com.foretell.sportsmeetings.controller;

import com.foretell.sportsmeetings.exception.InvalidProfilePhotoException;
import com.foretell.sportsmeetings.exception.RoleNotFoundException;
import com.foretell.sportsmeetings.exception.UserNotFoundException;
import com.foretell.sportsmeetings.exception.UsernameAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
            UserNotFoundException.class,
            RoleNotFoundException.class
    })
    public ResponseEntity<?> handleNotFoundException(Exception ex, WebRequest request) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(value = {
            UsernameAlreadyExistsException.class
    })
    public ResponseEntity<?> handleUsernameAlreadyExistsException(Exception ex, WebRequest request) {
        return ResponseEntity.status(409).body(ex.getMessage());
    }

    @ExceptionHandler(value = {
            InvalidProfilePhotoException.class,
            MaxUploadSizeExceededException.class
    })
    public ResponseEntity<?> handleInvalidProfilePhotoException(Exception ex, WebRequest request) {
        return ResponseEntity.status(400).body(ex.getMessage());
    }
}
