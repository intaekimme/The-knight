package com.a301.theknight.domain.game.dto.attack;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class AttackPlayerDto {
    @Positive(message = "Id can only be positive.")
    private long memberId;

    public AttackPlayerDto(long memberId){
        this.memberId = memberId;
    }
}
