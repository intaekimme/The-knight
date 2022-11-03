package com.a301.theknight.domain.game.dto.waiting.request;

import lombok.Data;

@Data
public class GameModifyRequest {
    private String title;
    private int maxUser;
    private int sword;
    private int twin;
    private int shield;
    private int hand;
}
