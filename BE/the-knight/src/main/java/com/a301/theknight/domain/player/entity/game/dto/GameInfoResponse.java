package com.a301.theknight.domain.player.entity.game.dto;

import lombok.Data;

@Data
public class GameInfoResponse {
    private long gameId;
    private String title;
    private int capacity;
    private int participant;
    private int sword;
    private int twin;
    private int shield;
    private int hand;
}
