package com.a301.theknight.domain.player.entity.game.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GameListDto {
    private long gameId;
    private String title;
    private String status;
    private int capacity;
    private int participant;
}
