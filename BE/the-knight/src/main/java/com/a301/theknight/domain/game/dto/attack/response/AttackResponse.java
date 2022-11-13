package com.a301.theknight.domain.game.dto.attack.response;

import com.a301.theknight.domain.game.dto.player.response.MemberTeamResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttackResponse {
    private MemberTeamResponse attacker;
    private MemberTeamResponse defender;
    private String weapon;
    private String hand;

}
