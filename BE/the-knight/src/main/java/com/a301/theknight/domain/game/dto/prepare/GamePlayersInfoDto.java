package com.a301.theknight.domain.game.dto.prepare;

import com.a301.theknight.domain.game.dto.prepare.response.GamePlayersInfoResponse;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GamePlayersInfoDto {
    private GamePlayersInfoResponse gamePlayersInfoResponseA;
    private GamePlayersInfoResponse gamePlayersInfoResponseB;
}
