package com.a301.theknight.domain.player.dto;

import lombok.Data;

import java.util.List;

@Data
public class PlayerReadyResponseList {
    private List<PlayerReadyResponse> playerReadyResponseList;

    public PlayerReadyResponseList(List<PlayerReadyResponse> playerReadyResponseList){
        this.playerReadyResponseList = playerReadyResponseList;
    }
}
