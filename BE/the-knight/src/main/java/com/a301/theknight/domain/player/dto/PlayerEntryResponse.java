package com.a301.theknight.domain.player.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerEntryResponse {
    private long playerId;
    private String nickname;
    private String image;

    @Builder
    public PlayerEntryResponse(long playerId, String nickname, String image){
        this.playerId = playerId;
        this.nickname = nickname;
        this.image = image;
    }
}
