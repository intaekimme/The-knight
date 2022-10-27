package com.a301.theknight.global.error.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GameErrorCode implements ErrorCode {

    GAME_IS_NOT_EXIST(HttpStatus.NOT_FOUND, "Game is not exist."),
    ALREADY_REGISTERED_GAME(HttpStatus.BAD_REQUEST, "Already registered game"),


    ;
    private final HttpStatus httpStatus;
    private final String message;
}

