package com.a301.theknight.domain.game.dto.prepare.response;

import com.a301.theknight.domain.game.dto.prepare.TeamLeaderDto;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GameLeaderDto {
    private TeamLeaderDto teamA;
    private TeamLeaderDto teamB;
}
