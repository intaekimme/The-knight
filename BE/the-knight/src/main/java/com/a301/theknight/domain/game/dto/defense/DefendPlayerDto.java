package com.a301.theknight.domain.game.dto.defense;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class DefendPlayerDto  {
    @Positive(message = "Id can only be positive.")
    private long id;

    public DefendPlayerDto(long id){
        this.id = id;
    }
}
