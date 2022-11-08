package com.a301.theknight.domain.game.dto.attack.request;

import com.a301.theknight.domain.game.dto.attack.AttackPlayerDto;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GameAttackPassRequest {
    @NotNull(message = "Attacker information is required.")
    private AttackPlayerDto attacker;
}
