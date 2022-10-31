package com.a301.theknight.domain.game.dto.playing.response;

import com.a301.theknight.domain.game.dto.playing.TeamLeaderDto;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GameLeaderDto {
    private TeamLeaderDto teamA;
    private TeamLeaderDto teamB;
}
