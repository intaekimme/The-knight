package com.a301.theknight.domain.game.dto.execute.response;

import com.a301.theknight.domain.game.entity.redis.Hand;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DefenderDto {
    private long memberId;
    private Hand hand;
    private Boolean isDead;
    private int hitCount;
    private boolean passedDefense;
}
