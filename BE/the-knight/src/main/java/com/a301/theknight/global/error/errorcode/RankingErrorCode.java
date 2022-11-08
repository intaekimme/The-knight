package com.a301.theknight.global.error.errorcode;

import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RankingErrorCode implements ErrorCode{

    RANKING_IS_NOT_EXIST(HttpStatus.NOT_FOUND, "Ranking is not exist")

    ;
    private final HttpStatus httpStatus;
    private final String message;
}
