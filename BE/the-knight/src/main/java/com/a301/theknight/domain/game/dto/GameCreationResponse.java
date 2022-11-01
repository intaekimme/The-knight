package com.a301.theknight.domain.game.dto;

import lombok.Data;

@Data
public class GameCreationResponse {
    private long newGameId;

    public GameCreationResponse(long newGameId){
        this.newGameId = newGameId;
    }
}
