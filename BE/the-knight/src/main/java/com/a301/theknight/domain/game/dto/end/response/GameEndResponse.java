package com.a301.theknight.domain.game.dto.end.response;

import com.a301.theknight.domain.game.dto.prepare.PlayerDataDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GameEndResponse {
    private String winningTeam;
    private long teamALeaderId;
    private long teamBLeaderId;
    private List<PlayerDataDto> players;
}
