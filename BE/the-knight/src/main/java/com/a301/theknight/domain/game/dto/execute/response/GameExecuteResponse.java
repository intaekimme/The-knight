package com.a301.theknight.domain.game.dto.execute.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GameExecuteResponse {
    private String attackTeam;
    private AttackerDto attacker;
    private DefenderDto defender;
}
