package com.a301.theknight.domain.game.dto;

import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.GameStatus;
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
        return Game.builder()
                .title(title)
                .gameStatus(GameStatus.WAITING)
                .sword(sword)
                .twin(twin)
                .shield(shield)
                .hand(hand)
                .capacity(capacity)
                .build();
    }
}
