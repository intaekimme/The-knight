package com.a301.theknight.domain.game.dto;

import lombok.Data;

@Data
public class GameCreationResponse {
    private long gameId;

    public GameCreationResponse(long gameId){
        this.gameId = gameId;
    }
}
