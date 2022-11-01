package com.a301.theknight.domain.player.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class PlayerTeamResponse {
    private long playerId;
    private String team;

    @Builder
    public PlayerTeamResponse(long playerId, String team){
        this.playerId = playerId;
        this.team = team;
    }
}
