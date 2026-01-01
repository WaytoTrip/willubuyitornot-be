package com.willu.buyitornot.exception;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(makeFinal = true)
public enum ErrorCode {
    // Common : 10000 ~ 19999
    UNAUTHORIZED(10000, "로그인이 필요합니다."),
    VALIDATION_FAILED(10001, "입력값이 올바르지 않습니다."),
    INVALID_PARAMETER(10002, "잘못된 파라미터입니다."),
    RESOURCE_NOT_FOUND(10003, "리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(10004, "서버 오류가 발생했습니다."),

    // User : 20000 ~ 29999
    USER_NOT_FOUND(20000, "사용자를 찾을 수 없습니다."),

    // Swipe : 30000 ~ 39999
    LATEST_SWIPE_NOT_FOUND(30000, "최신 스와이프를 찾을 수 없습니다."),
    SWIPE_NOT_FOUND(30001, "스와이프를 찾을 수 없습니다."),
    INVALID_GAME_IN_SWIPE(30002, "해당 스와이프에 포함되지 않는 게임입니다."),

    // Game : 40000 ~ 49999
    GAME_NOT_FOUND(40000, "조회된 게임이 없습니다."),

    // User Swipe : 50000 ~ 59999
    ALREADY_PARTICIPATED(50000, "이미 참여한 투표입니다."),

    ;

    int code;
    String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
