package com.a301.theknight.domain.game.dto.prepare;

import com.a301.theknight.domain.game.dto.prepare.response.GamePlayersInfoDto;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GamePlayersInfoResponse {
    private GamePlayersInfoDto playersAInfoDto;
    private GamePlayersInfoDto playersBInfoDto;
}
