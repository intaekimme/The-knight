package com.a301.theknight.domain.game.dto.defense.request;

import com.a301.theknight.domain.game.dto.attack.DefendPlayerDto;
import lombok.Data;

@Data
public class GameDefenseRequest {
    private DefendPlayerDto defender;
    private String weapon;
    private String hand;
}
