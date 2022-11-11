package com.a301.theknight.global.error.exception;

import com.a301.theknight.global.error.errorcode.ErrorCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class CustomWebSocketException extends RuntimeException implements CustomException {
    private final ErrorCode errorCode;

    public CustomWebSocketException(ErrorCode errorCode) {
        log.info("<<<< [Error Occur] : Error name = {}, status = {} message = {}", errorCode.name(), errorCode.getHttpStatus(), errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
