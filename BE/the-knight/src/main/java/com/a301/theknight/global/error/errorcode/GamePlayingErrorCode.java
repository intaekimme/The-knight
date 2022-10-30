package com.a301.theknight.global.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GamePlayingErrorCode implements ErrorCode {
    INGAME_IS_NOT_EXIST(HttpStatus.NOT_FOUND, "Ingame data is not exist."),

    ;
    private final HttpStatus httpStatus;
    private final String message;
}
