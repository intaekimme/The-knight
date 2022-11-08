package com.a301.theknight.domain.game.dto.doubt.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GameDoubtRequest {
    @NotNull(message = "DoubtPlayer information is required.")
    private DoubtPlayerIdDto suspected;

    //  TODO Enum타입으로 바꿀 것인지 고민
    @NotNull
    private String doubtStatus;
}
