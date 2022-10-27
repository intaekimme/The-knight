package com.a301.theknight.domain.player.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerEntryResponse {
    private long playerId;
    private String nickname;
    private String image;
}
