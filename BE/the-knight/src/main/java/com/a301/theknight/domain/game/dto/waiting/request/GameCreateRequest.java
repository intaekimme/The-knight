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
    private int maxMember;
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
                .capacity(maxMember)
                .build();
    }

    @Builder
    public GameCreateRequest(String title, int maxMember, int sword, int twin, int shield, int hand){
        this.title = title;
        this.maxMember = maxMember;
        this.sword = sword;
        this.twin = twin;
        this.shield = shield;
        this.hand = hand;
    }
}
