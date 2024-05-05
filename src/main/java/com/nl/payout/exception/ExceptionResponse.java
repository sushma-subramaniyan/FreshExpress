package com.nl.payout.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@Builder
public class ExceptionResponse {
    private int errorCode;
    private String errorMessage;

    private HttpStatus httpStatus;
}
