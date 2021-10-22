package com.foretell.sportsmeetings.controller.handler.exception;

import com.foretell.sportsmeetings.exception.InvalidDateTimeReqDtoException;
import com.foretell.sportsmeetings.exception.InvalidProfilePhotoException;
import com.foretell.sportsmeetings.exception.MeetingCategoryNotFoundException;
import com.foretell.sportsmeetings.exception.MeetingNotFoundException;
import com.foretell.sportsmeetings.exception.ProfileCommentException;
import com.foretell.sportsmeetings.exception.ProfileCommentNotFoundException;
import com.foretell.sportsmeetings.exception.RequestToJoinMeetingException;
import com.foretell.sportsmeetings.exception.RequestToJoinMeetingNotFoundException;
import com.foretell.sportsmeetings.exception.RoleNotFoundException;
import com.foretell.sportsmeetings.exception.UserNotFoundException;
import com.foretell.sportsmeetings.exception.UsernameAlreadyExistsException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> requestBody = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            requestBody.add(error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(requestBody);
    }


    @ExceptionHandler(value = {
            UserNotFoundException.class,
            RoleNotFoundException.class,
            ProfileCommentNotFoundException.class,
            MeetingCategoryNotFoundException.class,
            RequestToJoinMeetingNotFoundException.class,
            MeetingNotFoundException.class
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

    @ExceptionHandler(value = {
            ProfileCommentException.class,
    })
    public ResponseEntity<?> handleProfileCommentException(Exception ex, WebRequest request) {
        return ResponseEntity.status(400).body(ex.getMessage());
    }

    @ExceptionHandler(value = {
            InvalidDateTimeReqDtoException.class
    })
    public ResponseEntity<?> handleDateTimeReqDtoException(Exception ex, WebRequest request) {
        return ResponseEntity.status(400).body(ex.getMessage());
    }

    @ExceptionHandler(value = {
            RequestToJoinMeetingException.class
    })
    public ResponseEntity<?> handleRequestToJoinMeetingException(Exception ex, WebRequest request) {
        return ResponseEntity.status(400).body(ex.getMessage());
    }


}
