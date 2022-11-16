package com.a301.theknight.domain.player.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PlayerExitDto {
    private boolean ownerExited;
    private PlayerExitResponse playerExitResponse;
}
