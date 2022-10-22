package com.a301.theknight.domain.game.dto;

import lombok.Data;

@Data
public class GameInfoResponse {
    private int gameId;
    private String title;
    private int capacity;
    private int participant;
    private int sword;
    private int twin;
    private int shield;
    private int hand;
}
