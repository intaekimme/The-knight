package com.a301.theknight.domain.game.dto.waiting.request;

import com.a301.theknight.domain.game.entity.Game;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameCreateRequest {
    private String title;
    private int maxUser;

    private int currentUser;
    private int sword;
    private int twin;
    private int shield;
    private int hand;

    public Game toEntity(){
        return Game.builder()
                .title(title)
                .sword(sword)
                .twin(twin)
                .shield(shield)
                .hand(hand)
                .capacity(maxUser)
                .build();
    }

    @Builder
    public GameCreateRequest(String title, int maxUser, int sword, int twin, int shield, int hand){
        this.title = title;
        this.maxUser = maxUser;
        this.sword = sword;
        this.twin = twin;
        this.shield = shield;
        this.hand = hand;
    }
}
