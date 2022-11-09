package com.a301.theknight.domain.game.dto.doubt.request;

import com.a301.theknight.domain.game.entity.GameStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GameDoubtRequest {
    @NotNull(message = "DoubtPlayer information is required.")
    private DoubtPlayerIdDto suspected;

    @NotNull
    private GameStatus doubtStatus;
}
