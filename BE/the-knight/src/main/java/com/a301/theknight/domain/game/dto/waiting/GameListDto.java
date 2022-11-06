package com.a301.theknight.domain.game.dto.waiting;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GameListDto {
    private long gameId;
    private String title;
    private String status;
    private int maxMember;
    private int currentMembers;
}
