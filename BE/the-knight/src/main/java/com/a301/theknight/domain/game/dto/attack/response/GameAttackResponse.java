package com.a301.theknight.domain.game.dto.attack.response;

import com.a301.theknight.domain.game.dto.attack.AttackPlayerDto;
import com.a301.theknight.domain.game.dto.attack.DefendPlayerDto;

public class GameAttackResponse {
    private AttackPlayerDto attacker;
    private DefendPlayerDto defender;
    private String weapon;
    private String hand;
}
