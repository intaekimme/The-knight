package com.a301.theknight.domain.game.dto.waiting.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameInfoResponse {
    private long gameId;
    private String title;
    private int capacity;
    private int currentMembers;
    private int sword;
    private int twin;
    private int shield;
    private int hand;
}
