package com.a301.theknight.domain.game.entity.redis;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class TurnData implements Serializable {
    private long attackerId;
    private long defenderId;
    private AttackData attackData;
    private DefendData defendData;
    private boolean lyingAttack;
    private boolean lyingDefend;
}
