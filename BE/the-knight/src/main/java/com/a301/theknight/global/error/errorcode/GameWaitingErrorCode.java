package com.a301.theknight.global.error.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GameWaitingErrorCode implements ErrorCode {

    GAME_IS_NOT_READY_STATUS(HttpStatus.BAD_REQUEST, "The game room status is not in waiting status."),
    NO_PERMISSION_TO_DELETE_GAME_ROOM(HttpStatus.BAD_REQUEST, "Only the owner can delete a room."),
    NO_PERMISSION_TO_MODIFY_GAME_ROOM(HttpStatus.BAD_REQUEST, "Only the owner can modify a room."),
    CAN_NOT_ACCOMMODATE(HttpStatus.BAD_REQUEST, "Can't accommodate.")

    ;
    private final HttpStatus httpStatus;
    private final String message;
}
