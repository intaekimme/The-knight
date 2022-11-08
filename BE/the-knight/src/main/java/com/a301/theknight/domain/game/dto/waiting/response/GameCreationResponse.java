package com.a301.theknight.domain.game.dto.waiting.response;

import lombok.Data;

@Data
public class GameCreationResponse {
    private long gameId;

    public GameCreationResponse(long gameId){
        this.gameId = gameId;
    }
}
