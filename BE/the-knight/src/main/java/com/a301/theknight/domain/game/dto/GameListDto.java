package com.a301.theknight.domain.game.dto;

import lombok.Data;

@Data
public class GameListDto {
    private int gameId;
    private String title;
    private String status;
    private int capacity;
    private int participant;
}
