package com.a301.theknight.global.error.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DomainErrorCode implements ErrorCode {
    //========== Common ============
    DO_NOT_HAVE_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "You don't have an authentication."),
    DO_NOT_HAVE_AUTHORIZATION(HttpStatus.FORBIDDEN, "You don't have an authorization."),
    NO_SUCH_ELEMENTS(HttpStatus.NOT_FOUND, "Don't exist such resource."),

    ;
    private final HttpStatus httpStatus;
    private final String message;
}
