package com.newfit.reservation.exception;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ExceptionResponse {

    private final LocalDateTime timestamp;
    private final int code;
    private final String error;
    private final String message;
    private final String notice;



    @Builder(access = AccessLevel.PRIVATE)
    private ExceptionResponse(CustomException exception){
        this.timestamp = LocalDateTime.now();
        this.code = exception.getErrorCode().getStatusCode();
        this.error = exception.getErrorCode().name();
        this.message = exception.getErrorCode().getMessage();
        this.notice = exception.getNotice();
    }

    public static ExceptionResponse create(CustomException customException){
        return ExceptionResponse.builder()
                .exception(customException)
                .build();
    }
}
