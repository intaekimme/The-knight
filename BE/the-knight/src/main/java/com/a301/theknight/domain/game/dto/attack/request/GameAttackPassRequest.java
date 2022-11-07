package com.a301.theknight.domain.game.dto.attack.request;

import com.a301.theknight.domain.game.dto.attack.AttackPlayerDto;
import lombok.Data;

@Data
public class GameAttackPassRequest {
    private AttackPlayerDto attacker;
}
