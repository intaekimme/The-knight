package com.a301.theknight.domain.player.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class PlayerTeamResponse {
    private long memberId;
    private String team;

    @Builder
    public PlayerTeamResponse(long memberId, String team){
        this.memberId = memberId;
        this.team = team;
    }
}
