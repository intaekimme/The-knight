package com.a301.theknight.domain.game.entity.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoubtData {
    private long suspectId;
    private long suspectedId;
    private DoubtStatus doubtStatus;
    private Hand doubtHand;
    private boolean doubtSuccess;
    private boolean deadLeader;
    private int doubtPassCount;

    public void addPassCount() {
        doubtPassCount++;
    }
}
