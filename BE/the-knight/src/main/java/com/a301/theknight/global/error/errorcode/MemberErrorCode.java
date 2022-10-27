package com.a301.theknight.global.error.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    MEMBER_IS_NOT_EXIST(HttpStatus.NOT_FOUND, "Member is not exist."),
    ALREADY_REGISTERED_MEMBER(HttpStatus.BAD_REQUEST, "Already registered member"),


    ;
    private final HttpStatus httpStatus;
    private final String message;
}

