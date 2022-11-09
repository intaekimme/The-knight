package com.a301.theknight.domain.game.dto.waiting.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameModifyResponse {
    private String title;
    private int maxMember;
    private int sword;
    private int twin;
    private int shield;
    private int hand;
}
