package com.a301.theknight.domain.game.dto.defense.request;

import com.a301.theknight.domain.game.entity.redis.Hand;
import com.a301.theknight.global.validation.ValidEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GameDefenseRequest {
    @NotNull
    @ValidEnum(enumClass = Hand.class)
    private Hand hand;
}
