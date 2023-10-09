package com.shapoval.clearsolution.web.exceptionHandler;


import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.shapoval.clearsolution.error.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;


@RestControllerAdvice
public class UserControllerAdvice {


    @ExceptionHandler(UserWrongDateException.class)
    public ResponseEntity<UserErrorResponse> handleUserWrongDateException(UserWrongDateException exception
            , HttpServletRequest request) {

        return ResponseEntity.badRequest()
                .body(UserErrorResponse.builder()
                .error(ErrorTitle.WRONG_DATE.getError())
                .detail(exception.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build());


    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<UserErrorResponse> handleUserNotFoundException(UserNotFoundException exception
            , HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(UserErrorResponse.builder()
                .error(ErrorTitle.USER_NOT_FOUND.getError())
                .detail(exception.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build());


    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<UserErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception
            , HttpServletRequest request) {

        return ResponseEntity.badRequest()
                .body(UserErrorResponse.builder()
                        .error(ErrorTitle.VALIDATION_ERROR.getError())
                        .detail(exception.getMessage())
                        .path(request.getRequestURI())
                        .timestamp(LocalDateTime.now())
                        .build());

    }

    @ExceptionHandler(UserWrongAgeException.class)
    public ResponseEntity<UserErrorResponse> handleUserWrongAgeException(UserWrongAgeException exception
            , HttpServletRequest request) {


        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(UserErrorResponse.builder()
                        .error(ErrorTitle.WRONG_AGE.getError())
                        .detail(exception.getMessage())
                        .path(request.getRequestURI())
                        .timestamp(LocalDateTime.now())
                        .build());

    }

    @ExceptionHandler(UserEmailExistException.class)
    public ResponseEntity<UserErrorResponse> handleUserEmailExistException(UserEmailExistException exception
            , HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(UserErrorResponse.builder()
                        .error(ErrorTitle.USER_EMAIL_EXIST.getError())
                        .detail(exception.getMessage())
                        .path(request.getRequestURI())
                        .timestamp(LocalDateTime.now())
                        .build());

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)

    public ResponseEntity<UserErrorResponse> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception
            , HttpServletRequest request) {

        return ResponseEntity.badRequest()
                .body(UserErrorResponse.builder()
                        .error(ErrorTitle.VALIDATION_ERROR.getError())
                        .detail(Objects.requireNonNull(exception
                                        .getBindingResult()
                                        .getFieldError())
                                .getDefaultMessage())
                        .path(request.getRequestURI())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

}
