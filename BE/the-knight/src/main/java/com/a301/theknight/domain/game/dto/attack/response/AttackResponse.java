package com.a301.theknight.domain.game.dto.attack.response;

import com.a301.theknight.domain.game.dto.attack.AttackPlayerDto;
import com.a301.theknight.domain.game.dto.attack.DefendPlayerDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttackResponse {
    private AttackPlayerDto attacker;
    private DefendPlayerDto defender;
    private String weapon;
    private String hand;

    public static AttackResponse toResponse(AttackTeamResponse attackTeamResponse){
        return AttackResponse.builder()
                .attacker(attackTeamResponse.getAttacker())
                .defender(attackTeamResponse.getDefender())
                .weapon(attackTeamResponse.getWeapon())
                .hand(attackTeamResponse.getHand())
                .build();
    }
}
