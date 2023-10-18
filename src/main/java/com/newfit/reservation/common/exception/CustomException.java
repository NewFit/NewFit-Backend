package com.newfit.reservation.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String notice;


    public CustomException(ErrorCode errorCode){
        this.errorCode = errorCode;
        this.notice = "";
    }

    public CustomException(ErrorCode errorCode, String notice){
        this.errorCode = errorCode;
        this.notice = notice;

    }
}
