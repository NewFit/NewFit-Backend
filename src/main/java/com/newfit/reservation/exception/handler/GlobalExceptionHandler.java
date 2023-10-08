package com.newfit.reservation.exception.handler;


import com.newfit.reservation.exception.CustomException;
import com.newfit.reservation.exception.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ExceptionResponse> customException(CustomException exception){
        return ResponseEntity
                .status(exception.getErrorCode().getStatusCode())
                .body(ExceptionResponse.create(exception));
    }

    @ExceptionHandler(RuntimeException.class)
    protected  ResponseEntity<RuntimeException> internalServerException(RuntimeException exception){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception);
    }
}
