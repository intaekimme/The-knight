package com.a301.theknight.domain.game.dto.attack;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class AttackPlayerDto {
    @Positive(message = "Id can only be positive.")
    private long id;

    public AttackPlayerDto(long id){
        this.id = id;
    }
}
