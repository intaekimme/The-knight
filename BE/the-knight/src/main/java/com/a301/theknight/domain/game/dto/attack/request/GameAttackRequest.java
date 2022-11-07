package com.a301.theknight.domain.game.dto.attack.request;

import com.a301.theknight.domain.game.dto.attack.AttackPlayerDto;
import com.a301.theknight.domain.game.dto.attack.DefendPlayerDto;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GameAttackRequest {
    @NotNull(message = "Attacker information is required.")
    private AttackPlayerDto attacker;
    @NotNull(message = "Defender information is required.")
    private DefendPlayerDto defender;

    //  TODO Enum 타입으로 바꾸고 valid 체크하기
    @NotNull
    private String weapon;
    @NotNull
    private String hand;
}
