package com.newfit.reservation.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private final ErrorCodeType errorCodeType;
    private final String notice;


    public CustomException(ErrorCodeType errorCodeType){
        this.errorCodeType = errorCodeType;
        this.notice = "";
    }

    public CustomException(ErrorCodeType errorCodeType, String notice){
        this.errorCodeType = errorCodeType;
        this.notice = notice;

    }
}
