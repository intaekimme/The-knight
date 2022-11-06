package com.a301.theknight.domain.game.entity.redis;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DoubtData {
    private long suspectId;
    private long suspectedId;
    private DoubtStatus doubtStatus;
    private Hand doubtHand;
    private boolean doubtResult;
    private boolean deadLeader;
}
