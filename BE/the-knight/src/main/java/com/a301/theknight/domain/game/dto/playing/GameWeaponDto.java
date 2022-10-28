package com.a301.theknight.domain.game.dto.playing;

import com.a301.theknight.domain.game.entity.Game;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class GameWeaponDto implements Serializable {
    private int sword;
    private int twin;
    private int shield;
    private int hand;

    public static GameWeaponDto toDto(Game game) {
        return GameWeaponDto.builder()
                .sword(game.getSword())
                .shield(game.getShield())
                .twin(game.getTwin())
                .hand(game.getHand()).build();
    }
}
