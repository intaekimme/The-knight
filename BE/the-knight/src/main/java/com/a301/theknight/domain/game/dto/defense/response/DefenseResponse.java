package com.a301.theknight.domain.game.dto.defense.response;

import com.a301.theknight.domain.game.dto.attack.DefendPlayerDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DefenseResponse {
    private DefendPlayerDto defender;
    private String weapon;
    private String hand;

    public static DefenseResponse toResponse(DefenseTeamResponse defenseTeamResponse){
        return DefenseResponse.builder()
                .defender(defenseTeamResponse.getDefender())
                .weapon(defenseTeamResponse.getWeapon())
                .hand(defenseTeamResponse.getHand())
                .build();
    }
}
