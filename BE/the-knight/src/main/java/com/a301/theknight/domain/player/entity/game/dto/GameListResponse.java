package com.a301.theknight.domain.player.entity.game.dto;

import lombok.Data;

import java.util.List;

@Data
public class GameListResponse {
    private List<GameListDto> games;
}
