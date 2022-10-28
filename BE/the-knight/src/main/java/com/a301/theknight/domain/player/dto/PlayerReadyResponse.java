package com.a301.theknight.domain.player.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PlayerReadyResponse {
    private long playerId;
    private boolean readyStatus;
    private boolean startFlag;
}
