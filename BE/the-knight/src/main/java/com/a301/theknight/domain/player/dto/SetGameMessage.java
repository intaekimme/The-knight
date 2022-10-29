package com.a301.theknight.domain.player.dto;

import lombok.Data;

@Data
public class SetGameMessage {
    private String setGame;
    public SetGameMessage(String setGame){this.setGame = setGame;}
}
