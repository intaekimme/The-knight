package com.a301.theknight.domain.game.dto.attack;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Positive;

@AllArgsConstructor
@Data
public class DefendPlayerIdDto {
    @Positive(message = "Id can only be positive.")
    private long memberId;
}
