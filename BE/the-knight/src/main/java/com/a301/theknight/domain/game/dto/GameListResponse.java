package com.a301.theknight.domain.game.dto;

import lombok.Data;

import java.util.List;

@Data
public class GameListResponse {
    private List<GameListDto> games;
}
