package com.a301.theknight.domain.game.dto.prepare.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameOrderDto {
    private long memberId;
    private String nickname;
    private String image;
}
