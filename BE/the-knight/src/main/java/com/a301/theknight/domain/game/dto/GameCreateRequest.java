package com.a301.theknight.domain.game.dto;

import com.a301.theknight.domain.game.entity.Game;
import lombok.Data;

@Data
public class GameCreateRequest {
    private String title;
    private int capacity;
    private int sword;
    private int twin;
    private int shield;
    private int hand;

    public Game toEntity(){
        return
    }
}
