package com.a301.theknight.domain.game.dto.attack;

import lombok.Data;

@Data
public class DefendPlayerDto {
    private long id;

    public DefendPlayerDto(long id){
        this.id = id;
    }
}
