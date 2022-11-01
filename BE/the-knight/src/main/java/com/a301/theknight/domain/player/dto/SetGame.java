package com.a301.theknight.domain.player.dto;

import lombok.Data;

@Data
public class SetGame {
    private String setGame;

    public SetGame(String setGame){
        this.setGame = setGame;
    }
}
