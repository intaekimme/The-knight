package com.a301.theknight.domain.game.dto.execute.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DefenderDto {
    private long memberId;
    private String hand;
    private Boolean isDead;
    private int restCount;
    private boolean passedDefense;
}
