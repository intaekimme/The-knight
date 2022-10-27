package com.a301.theknight.domain.player.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PlayerTeamResponse {
    long playerId;
    String team;
}
