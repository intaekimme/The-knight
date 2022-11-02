package com.a301.theknight.domain.player.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReadyResponseDto {
    private List<PlayerReadyResponse> playerReadyList;
    private String setGame;
}
