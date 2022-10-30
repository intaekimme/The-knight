package com.a301.theknight.global.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GamePlayingErrorCode implements ErrorCode {
    INGAME_IS_NOT_EXIST(HttpStatus.NOT_FOUND, "Ingame data is not exist."),
    NOT_ENOUGH_WEAPON(HttpStatus.BAD_REQUEST, "Weapon is not enough"),
    SELECT_WEAPON_IS_FULL(HttpStatus.BAD_REQUEST, "All selected weapons are full."),
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
