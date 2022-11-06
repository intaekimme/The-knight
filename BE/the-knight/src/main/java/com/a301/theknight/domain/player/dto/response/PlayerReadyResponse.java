package com.a301.theknight.domain.player.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PlayerReadyResponse {
    private long playerId;
    private boolean readyStatus;
}
