package com.a301.theknight.global.error.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GameWaitingErrorCode implements ErrorCode {

    GAME_IS_NOT_READY_STATUS(HttpStatus.BAD_REQUEST, "The game room status is not in waiting status."),
    PLAYER_IS_ALREADY_ENTRY(HttpStatus.CONFLICT, "This Player is already entry game."),
    NO_PERMISSION_TO_DELETE_GAME_ROOM(HttpStatus.BAD_REQUEST, "Only the owner can delete a room."),
    NO_PERMISSION_TO_MODIFY_GAME_ROOM(HttpStatus.BAD_REQUEST, "Only the owner can modify a room."),
    CAN_NOT_ACCOMMODATE(HttpStatus.BAD_REQUEST, "Can't accommodate."),
    NUMBER_OF_PLAYERS_ON_BOTH_TEAM_IS_DIFFERENT(HttpStatus.BAD_REQUEST, "The number of players on both team is different."),
    NOT_All_USERS_ARE_READY(HttpStatus.BAD_REQUEST, "Not all users are ready."),
    NOT_MET_ALL_THE_CONDITIONS_YET(HttpStatus.BAD_REQUEST, "Not met all the conditions yet."),
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
