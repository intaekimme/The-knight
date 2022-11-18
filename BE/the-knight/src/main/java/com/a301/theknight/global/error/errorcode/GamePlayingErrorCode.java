package com.a301.theknight.global.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GamePlayingErrorCode implements ErrorCode {
    INGAME_IS_NOT_EXIST(HttpStatus.NOT_FOUND, "Ingame data is not exist."),
    INGAME_PLAYER_IS_NOT_EXIST(HttpStatus.NOT_FOUND, "Player data is not exist."),
    WRONG_GAME_STATUS(HttpStatus.CONFLICT, "Request wrong game status."),
    WEAPON_DATA_IS_NOT_EXIST(HttpStatus.NOT_FOUND, "Weapon data is not exist."),
    CAN_NOT_PLAYING_GAME(HttpStatus.BAD_REQUEST, "Can't prepare game data. Maybe all player do not ready."),
    NOT_ENOUGH_WEAPON(HttpStatus.BAD_REQUEST, "Weapon is not enough"),
    SELECT_WEAPON_IS_FULL(HttpStatus.BAD_REQUEST, "All selected weapons are full."),
    ORDER_NUMBER_IS_INVALID(HttpStatus.BAD_REQUEST, "Order number value is invalid."),
    NOT_MATCH_REQUEST_TEAM(HttpStatus.BAD_REQUEST, "Not matching from request user and data team."),
    ALREADY_SELECTED_ORDER_NUMBER(HttpStatus.BAD_REQUEST, "Already selected order number."),
    CAN_COMPLETE_BY_LEADER(HttpStatus.FORBIDDEN, "Only leader can complete select."),
    CAN_NOT_COMPLETE_ORDER_SELECT(HttpStatus.CONFLICT, "Can not complete order select."),
    CAN_NOT_COMPLETE_WEAPON_SELECT(HttpStatus.CONFLICT, "Can not complete weapon select."),
    LEADER_IS_NOT_SELECTED(HttpStatus.BAD_REQUEST, "Team leader is not selected."),
    PLAYER_IS_ALREADY_DEAD(HttpStatus.BAD_REQUEST, "Player is already dead."),
    CAN_NOT_DOUBT_SAME_TEAM(HttpStatus.BAD_REQUEST, "Can not doubt same team."),
    DO_NOT_FIT_REQUEST_BY_GAME_STATUS(HttpStatus.BAD_REQUEST, "Bad request that doesn't fit the game state."),
    PLAYER_IS_NOT_USER_WHO_LOGGED_IN(HttpStatus.BAD_REQUEST, "The current player is not the user who logged in."),
    UNABLE_TO_PASS_ATTACK(HttpStatus.BAD_REQUEST, "Attack pass cannot be made."),
    UNABLE_TO_PASS_DEFENSE(HttpStatus.BAD_REQUEST, "Defense pass cannot be made."),
    UNABLE_TO_PASS_DOUBT(HttpStatus.BAD_REQUEST, "Doubt pass cannot be made."),
    ALL_TEAM_PLAYER_IS_DEAD(HttpStatus.CONFLICT, "All Players is dead."),
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
