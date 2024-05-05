package com.nl.payout.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.concurrent.atomic.AtomicReference;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler  {


    /**
     *
     * @param e methodArgumentNotValidException
     * @return bad request
     */

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        AtomicReference<String> errorMessage = new AtomicReference<>("");
        e.getBindingResult().getAllErrors().forEach(error -> errorMessage.set(error.getDefaultMessage()));
        log.error("Bad Request {}", e.getMessage());
        ExceptionResponse errorResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), errorMessage.get(),HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     *
     * @param e ServiceException
     * @return bad request
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ExceptionResponse> handleServiceException(Exception e) {
        ExceptionResponse errorResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(),HttpStatus.BAD_REQUEST);
        log.error("Bad Request {}", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }




    /**
     *
     * @param e RuntimeException
     * @return Bad Request
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleInternalError(RuntimeException e) {
        ExceptionResponse errorResponse = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
        log.error("Internal Server Error{}", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }









}