package com.a301.theknight.domain.game.dto.defense.response;

import com.a301.theknight.domain.game.dto.attack.DefendPlayerDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DefenseTeamResponse {
    private DefendPlayerDto defender;
    private String weapon;
    private String hand;
    private String team;
}
