package com.newfit.reservation.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {


    // 404 NOT_FOUND 잘못된 리소스 접근
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    GYM_NOT_FOUND(HttpStatus.NOT_FOUND, "헬스장을 찾을 수 없습니다."),
    AUTHORITY_NOT_FOUND(HttpStatus.NOT_FOUND, "권한을 찾을 수 없습니다."),
    EQUIPMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "기구 종류를 찾을 수 없습니다."),
    EQUIPMENT_GYM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 기구를 찾을 수 없습니다."),
    EQUIPMENT_ROUTINE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 루틴 기구를 찾을 수 없습니다."),
    ROUTINE_NOT_FOUND(HttpStatus.NOT_FOUND, "루틴을 찾을 수 없습니다."),
    OAUTH_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "OAuth 내역을 찾을 수 없습니다."),

    // 409 CONFLICT
    ALREADY_ACCEPTED_AUTHORITY(HttpStatus.CONFLICT, "이미 승인된 사용자입니다."),
    DUPLICATE_ROUTINE_NAME(HttpStatus.CONFLICT, "중복된 루틴 이름입니다."),

    // 500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다.")
    ;

    private final String name;
    private final int statusCode;
    private final String message;

    ErrorCode(HttpStatus status, String message){
        this.name = status.name();
        this.statusCode = status.value();
        this.message = message;
    }
}
