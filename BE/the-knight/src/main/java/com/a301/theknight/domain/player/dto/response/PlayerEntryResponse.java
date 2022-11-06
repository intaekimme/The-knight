package com.a301.theknight.domain.player.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerEntryResponse {
    private long memberId;
    private String nickname;
    private String image;

    @Builder
    public PlayerEntryResponse(long memberId, String nickname, String image){
        this.memberId = memberId;
        this.nickname = nickname;
        this.image = image;
    }
}
