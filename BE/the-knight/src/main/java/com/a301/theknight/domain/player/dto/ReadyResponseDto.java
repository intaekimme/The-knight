package com.a301.theknight.domain.player.dto;

import lombok.Data;

@Data
public class ReadyResponseDto {
    private PlayerReadyResponse playerReadyResponse;
    private OwnerReadyResponse ownerReadyResponse;
    private boolean isOwner = false;
}
