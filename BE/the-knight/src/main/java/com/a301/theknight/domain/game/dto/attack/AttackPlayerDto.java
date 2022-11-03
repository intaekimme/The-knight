package com.a301.theknight.domain.game.dto.attack;

import lombok.Data;

@Data
public class AttackPlayerDto {
    private long id;

    public AttackPlayerDto(long id){
        this.id = id;
    }
}
