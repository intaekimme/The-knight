package com.a301.theknight.domain.game.dto.attack.request;

import com.a301.theknight.domain.game.dto.attack.AttackPlayerDto;
import com.a301.theknight.domain.game.dto.attack.DefendPlayerDto;
import lombok.Data;

@Data
public class GameAttackRequest {
    private AttackPlayerDto attacker;
    private DefendPlayerDto defender;
    private String weapon;
    private String hand;
}
