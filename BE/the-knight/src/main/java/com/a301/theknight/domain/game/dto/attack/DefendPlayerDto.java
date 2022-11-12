package com.a301.theknight.domain.game.dto.attack;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class DefendPlayerDto {
    @Positive(message = "Id can only be positive.")
    private long memberId;

    public DefendPlayerDto(long memberId){
        this.memberId = memberId;
    }
}
