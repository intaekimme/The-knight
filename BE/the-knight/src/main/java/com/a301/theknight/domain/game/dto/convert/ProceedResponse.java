package com.a301.theknight.domain.game.dto.convert;

import com.a301.theknight.domain.game.entity.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProceedResponse {
    private GameStatus gameStatus;
    private long limitTime;

    public static ProceedResponse toDto(GameStatus gameStatus) {
        return new ProceedResponse(gameStatus, gameStatus.getLimitMilliSeconds());
    }
}
