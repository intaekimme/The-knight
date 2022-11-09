package com.a301.theknight.global.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChattingErrorCode implements ErrorCode {
    CAN_NOT_SEND_OTHER_TEAM(HttpStatus.BAD_REQUEST, "Can not send chatting to other team."),
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
