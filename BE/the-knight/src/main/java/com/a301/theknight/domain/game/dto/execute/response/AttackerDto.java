package com.a301.theknight.domain.game.dto.execute.response;

import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.game.entity.redis.Hand;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AttackerDto {
    private long memberId;
    private Weapon weapon;
    private Hand hand;
}
