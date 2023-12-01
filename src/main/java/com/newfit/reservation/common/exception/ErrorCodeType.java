package com.newfit.reservation.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCodeType {


    // 400 BAD_REQUEST
    INCOMPATIBLE_EQUIPMENT_NAME_COUNT(HttpStatus.BAD_REQUEST, "기구 이름 개수가 부족합니다."),
    EXPIRED_TAG(HttpStatus.BAD_REQUEST, "최근 태그 시간이 2시간 이전입니다."),
    INVALID_RESERVATION_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 예약 요청입니다."),
    INVALID_CREDIT_ACQUIRE_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 크레딧 획득 요청입니다."),
    INVALID_TAG_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 태그 요청입니다."),
    INVALID_SEQUENCE(HttpStatus.BAD_REQUEST, "잘못된 sequence 값입니다."),
    INVALID_ENUM_VALUE(HttpStatus.BAD_REQUEST, "잘못된 enum value 입니다."),

    // 401 UNAUTHORIZED
    UNAUTHORIZED_REQUEST(HttpStatus.UNAUTHORIZED, "인가에 실패하였습니다."),
    ADMIN_UNAUTHORIZED_REQUEST(HttpStatus.UNAUTHORIZED, "어드민 인가에 실패하였습니다."),

    // 404 NOT_FOUND 잘못된 리소스 접근
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    GYM_NOT_FOUND(HttpStatus.NOT_FOUND, "헬스장을 찾을 수 없습니다."),
    AUTHORITY_NOT_FOUND(HttpStatus.NOT_FOUND, "권한을 찾을 수 없습니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다."),
    EQUIPMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "기구 종류를 찾을 수 없습니다."),
    EQUIPMENT_GYM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 기구를 찾을 수 없습니다."),
    EQUIPMENT_ROUTINE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 루틴 기구를 찾을 수 없습니다."),
    ROUTINE_NOT_FOUND(HttpStatus.NOT_FOUND, "루틴을 찾을 수 없습니다."),
    OAUTH_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "OAuth 내역을 찾을 수 없습니다."),
    CREDIT_NOT_FOUND(HttpStatus.NOT_FOUND, "크레딧을 찾을 수 없습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "리프레쉬 토큰을 찾을 수 없습니다."),
    ADMIN_COOKIE_NOT_FOUND(HttpStatus.NOT_FOUND, "어드민 쿠키를 찾을 수 없습니다."),

    // 409 CONFLICT
    ALREADY_ACCEPTED_AUTHORITY(HttpStatus.CONFLICT, "이미 승인된 사용자입니다."),
    DUPLICATE_ROUTINE_NAME(HttpStatus.CONFLICT, "중복된 루틴 이름입니다."),
    DUPLICATE_AUTHORITY_REQUEST(HttpStatus.CONFLICT, "중복된 헬스장 등록 요청입니다."),
    ALREADY_TAGGED_RESERVATION(HttpStatus.CONFLICT, "이미 태그하였습니다."),
    ALREADY_RESERVED_EQUIPMENT_GYM(HttpStatus.CONFLICT, "이미 다른 사용자가 해당 시간에 예약했습니다."),
    ALREADY_EXISTING_EQUIPMENT(HttpStatus.CONFLICT, "이미 존재하는 기구입니다."),
    MAXIMUM_CREDIT_LIMIT(HttpStatus.CONFLICT, "일일 크레딧 획득량을 모두 채웠습니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "중복된 닉네임입니다."),
    AUTHORITY_ID_LIST_OUTDATED(HttpStatus.CONFLICT, "토큰의 AuthorityIdList에 변경 사항이 있습니다"),
    USER_EMAIL_VERIFICATION_FAIL(HttpStatus.CONFLICT, "이메일이 일치하지 않습니다."),


    // 500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다.");

    private final String name;
    private final int statusCode;
    private final String message;

    ErrorCodeType(HttpStatus status, String message) {
        this.name = status.name();
        this.statusCode = status.value();
        this.message = message;
    }
}
