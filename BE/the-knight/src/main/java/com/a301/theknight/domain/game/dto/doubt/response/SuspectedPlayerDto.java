package com.a301.theknight.domain.game.dto.doubt.response;

import com.a301.theknight.domain.game.entity.redis.Hand;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SuspectedPlayerDto {
    private long id;
    private String weapon;
    private String hand;
    private Boolean isDead;

    public static SuspectedPlayerDto toDto(InGamePlayer suspected, Hand hand) {
        return SuspectedPlayerDto.builder()
                .id(suspected.getMemberId())
                .hand(hand.name())
                .weapon(Hand.LEFT.equals(hand) ? suspected.getLeftWeapon().name() : suspected.getRightWeapon().name())
                .isDead(suspected.isDead())
                .build();
    }
}