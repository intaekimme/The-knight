package com.a301.theknight.global.error.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PlayerErrorCode implements ErrorCode {

    PLAYER_IS_NOT_EXIST(HttpStatus.NOT_FOUND, "Player is not exist.")

    ;
    private final HttpStatus httpStatus;
    private final String message;
}

