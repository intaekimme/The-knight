package com.a301.theknight.domain.game.dto;

import lombok.Data;

@Data
public class GameModifyRequest {
    String title;
    int capacity;
    int sword;
    int twin;
    int shield;
    int hand;
}
