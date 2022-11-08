package com.a301.theknight.domain.game.dto.attacker.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttackerResponse {
    private long memberId;
    private boolean isOpposite;

//    public static AttackerResponse toResponse(AttackTeamResponse attackTeamResponse){
//        return AttackerResponse.builder()
//                .attacker(attackTeamResponse.getAttacker())
//                .defender(attackTeamResponse.getDefender())
//                .weapon(attackTeamResponse.getWeapon())
//                .hand(attackTeamResponse.getHand())
//                .build();
//    }
}
