package com.a301.theknight.domain.game.dto.waiting.response;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class GameExitResponse {
    private boolean exit;
}
