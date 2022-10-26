package com.a301.theknight.domain.player.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerEntryResponse {
    long playerId;
    String nickname;
    String image;
}
